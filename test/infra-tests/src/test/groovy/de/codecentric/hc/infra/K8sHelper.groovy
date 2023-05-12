package de.codecentric.hc.infra

import groovy.transform.Immutable
import io.kubernetes.client.Exec
import io.kubernetes.client.openapi.ApiClient
import io.kubernetes.client.openapi.ApiException
import io.kubernetes.client.openapi.Configuration
import io.kubernetes.client.openapi.apis.CoreV1Api
import io.kubernetes.client.openapi.models.V1Pod
import io.kubernetes.client.openapi.models.V1PodBuilder
import io.kubernetes.client.util.Config
import io.kubernetes.client.util.Streams

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class K8sHelper {
    private static int WAIT_LOOP_SLEEP_MS = 500

    ApiClient client
    CoreV1Api api


    K8sHelper() {
        client = Config.defaultClient()
        Configuration.setDefaultApiClient(client)
        api = new CoreV1Api()
    }


    V1Pod createAlpineCmdRunnerWithPackages(
            String podName,
            String namespace,
            Map<String, String> labels,
            List<String> packages
    ) {


        V1Pod pod = new V1PodBuilder()
                .withNewMetadata()
                .withName(podName)
                .withLabels(labels)
                .endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withNewReadinessProbe()
                .withNewExec()
                .withCommand("/bin/sh", "-c",
                        "test -f /ready"
                )
                .endExec()
                .withInitialDelaySeconds(5)
                .withPeriodSeconds(2)
                .endReadinessProbe()
                .withName("alpine-cli")
                .withImage("alpine:3")
                .withCommand("/bin/sh")
                .withArgs("-c", "apk add --no-cache ${packages.join(' ')} && touch /ready && while true; do sleep 10; done")
                .endContainer()
                .endSpec()
                .build()

        try {
            api.createNamespacedPod(namespace, pod, null, null, null, null)
        }
        catch (ApiException e) {
            if (e.code == 409) {
                deletePod(podName, namespace)
                api.createNamespacedPod(namespace, pod, null, null, null, null)
            } else {
                throw e
            }
        }
    }

    def deletePod(String podName, String namespace) {
        api.deleteNamespacedPod(podName, namespace, null, null, 0, null, "Foreground", null)
        waitForPodDisappearance(podName, namespace)
    }

    boolean waitForPodDisappearance(String podName, String namespace) {
        def gone = false
        while (!gone) {
            sleep(WAIT_LOOP_SLEEP_MS)
            try {
                api.readNamespacedPodStatus(podName, namespace, null)
            } catch (ApiException e) {
                gone = e.code == 404
            }
        }
        gone
    }

    def waitForReadyPod(String podName, String namespace) {
        def ready = false
        while (!ready) {
            sleep(WAIT_LOOP_SLEEP_MS)
            def status = api.readNamespacedPodStatus(podName, namespace, null)
            ready = status.status.conditions.find { it.type == 'ContainersReady' && it.status == 'True' } != null
        }
    }

    ProcessResult executeCommandInPod(String podName, String namespace, String command) {
        Exec exec = new Exec(client)
        def args = [
                "/bin/sh",
                "-c",
                command
        ]

        final Process proc = exec.exec(namespace, podName, args.toArray() as String[], "alpine-cli", false, false)

        def pool = Executors.newFixedThreadPool(2)
        def stdoutFuture = pool.submit(new InputStreamToStringCallable(proc.getInputStream()))
        def stderrFuture = pool.submit(new InputStreamToStringCallable(proc.getErrorStream()))
        pool.shutdown()
        def finished = pool.awaitTermination(1, TimeUnit.SECONDS)
        assert finished, "thread pool finished properly"
        def stderr = stderrFuture.get()
        def stdout = stdoutFuture.get()

        proc.waitFor()
        // wait for any last output; no need to wait for input thread

        proc.destroy()

        new ProcessResult(proc.exitValue(), stdout, stderr)
    }

    @Immutable
    class ProcessResult {
        int code
        String stdout
        String stderr
    }

    class InputStreamToStringCallable implements Callable<String> {
        InputStreamReader reader

        InputStreamToStringCallable(InputStream is) {
            this.reader = new InputStreamReader(is, "UTF-8")
        }

        @Override
        String call() throws Exception {
            Streams.toString(reader)
        }
    }
}
