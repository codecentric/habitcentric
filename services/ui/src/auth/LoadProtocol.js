export function loadProtocol(url) {
  const urlWithActualProtocol = new URL(addDefaultProtocolIfMissing(url));
  const actualProtocol = window.location.protocol;
  urlWithActualProtocol.protocol = actualProtocol;
  return urlWithActualProtocol.href;
}

function addDefaultProtocolIfMissing(url) {
  if (!/^(?:f|ht)tps?:\/\//.test(url)) {
    url = "http://" + url;
  }
  return url;
}
