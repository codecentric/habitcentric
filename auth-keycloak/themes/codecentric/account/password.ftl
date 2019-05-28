<#import "template.ftl" as layout>
<@layout.mainLayout active='password' bodyClass='password'; section>

    <div class="row">
        <div class="col-md-10">
            <h2>${msg("changePasswordHtmlTitle")}</h2>
        </div>
        <div class="col-md-2 subtitle">
            <span class="subtitle">${msg("allFieldsRequired")}</span>
        </div>
    </div>

    <form action="${url.passwordUrl}" class="form-horizontal" method="post">
        <input type="text" id="username" name="username" value="${(account.username!'')}" autocomplete="username" readonly="readonly" style="display:none;">

        <#if password.passwordSet>
            <div class="form-group">
                <div class="col-sm-2 col-md-2">
                    <label for="password" class="control-label">${msg("password")}</label>
                </div>

                <div class="col-sm-10 col-md-10">
                    <input type="password" class="form-control" id="password" name="password" autofocus autocomplete="current-password">
                </div>
            </div>
        </#if>

        <input type="hidden" id="stateChecker" name="stateChecker" value="${stateChecker}">

        <div class="form-group">
            <div class="col-sm-2 col-md-2">
                <label for="password-new" class="control-label">${msg("passwordNew")}</label>
            </div>

            <div class="col-sm-10 col-md-10">
                <input type="password" class="form-control" id="password-new" name="password-new" autocomplete="new-password">
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-2 col-md-2">
                <label for="password-confirm" class="control-label" class="two-lines">${msg("passwordConfirm")}</label>
            </div>

            <div class="col-sm-10 col-md-10">
                <input type="password" class="form-control" id="password-confirm" name="password-confirm" autocomplete="new-password">
            </div>
        </div>

        <div class="form-group">
            <div id="kc-form-buttons" class="col-md-offset-2 col-md-10 submit">
                <div class="">
                    <button type="submit" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}" name="submitAction" value="Save">${msg("doSave")}</button>
                </div>
            </div>
        </div>
    </form>

   <h2>For windows-WLAN-users only</h2>

<P>
After changing the password in the form above you have to retype your password to reset the password hashes. 
This is mandatory to get access to the WLAN <em>cc-wlan</em> if you are really using Windows 10.
Currently this is only possible from inside.
<P>
    <form action="http://10.10.70.30/cgi-bin/pwd.pl" method="POST">
        <div class="form-group">
                <div class="col-sm-2 col-md-2">
                    <label for="uid" class="control-label">UserID</label>
                </div>
            <div class="col-sm-10 col-md-10">
                   <INPUT type="text" name="uid" value="${(account.username!'')}" class="form-control">
            </div>
        </div>

        <div class="form-group">
                <div class="col-sm-2 col-md-2">
                    <label for="pwd" class="control-label">${msg("password")}</label>
                </div>
            <div class="col-sm-10 col-md-10">
                  <INPUT type="password" name="pwd" class="form-control">
            </div>
        </div>

        <div class="form-group">
            <div id="kc-form-buttons" class="col-md-offset-2 col-md-10 submit">
                <div class="">
                   <INPUT type="submit" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}">
                </div>
            </div>
        </div>
    </form>
</@layout.mainLayout>
