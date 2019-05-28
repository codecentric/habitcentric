<#import "template.ftl" as layout>
<@layout.mainLayout active='account' bodyClass='user'; section>

    <div class="row">
        <div class="col-md-10">
            <h2>${msg("editAccountHtmlTitle")}</h2>
        </div>
        <div class="col-md-2 subtitle">
            <span class="subtitle"><span class="required">*</span> ${msg("requiredFields")}</span>
        </div>
    </div>

    <form action="${url.accountUrl}" class="form-horizontal" method="post">

        <input type="hidden" id="stateChecker" name="stateChecker" value="${stateChecker}">

        <#if !realm.registrationEmailAsUsername>
            <div class="form-group ${messagesPerField.printIfExists('username','has-error')}">
                <div class="col-sm-2 col-md-2">
                    <label for="username" class="control-label">${msg("username")}</label> <#if realm.editUsernameAllowed><span class="required">*</span></#if>
                </div>

                <div class="col-sm-10 col-md-10">
                    <input type="text" class="form-control" id="username" name="username" <#if !realm.editUsernameAllowed>disabled="disabled"</#if> value="${(account.username!'')}"/>
                </div>
            </div>
        </#if>

        <div class="form-group ${messagesPerField.printIfExists('email','has-error')}">
            <div class="col-sm-2 col-md-2">
            <label for="email" class="control-label">${msg("email")}</label> <span class="required">*</span>
            </div>

            <div class="col-sm-10 col-md-10">
                <input type="text" class="form-control" id="email" name="email" autofocus value="${(account.email!'')}"/>
            </div>
        </div>

        <div class="form-group ${messagesPerField.printIfExists('firstName','has-error')}">
            <div class="col-sm-2 col-md-2">
                <label for="firstName" class="control-label">${msg("firstName")}</label> <span class="required">*</span>
            </div>

            <div class="col-sm-10 col-md-10">
                <input type="text" class="form-control" id="firstName" name="firstName" value="${(account.firstName!'')}"/>
            </div>
        </div>

        <div class="form-group ${messagesPerField.printIfExists('lastName','has-error')}">
            <div class="col-sm-2 col-md-2">
                <label for="lastName" class="control-label">${msg("lastName")}</label> <span class="required">*</span>
            </div>

            <div class="col-sm-10 col-md-10">
                <input type="text" class="form-control" id="lastName" name="lastName" value="${(account.lastName!'')}"/>
            </div>
        </div>

        <div class="form-group ${messagesPerField.printIfExists('phoneNumber','has-error')}">
            <div class="col-sm-2 col-md-2">
                <label for="lastName" class="control-label">${msg("phoneNumber")}</label>
            </div>

            <div class="col-sm-10 col-md-10">
                <input type="text" class="form-control" id="user.attributes.phoneNumber" name="user.attributes.phoneNumber" value="${(account.attributes.phoneNumber!'')}"/>
            </div>
        </div>

        <div class="form-group ${messagesPerField.printIfExists('licensePlateNumber','has-error')}">
            <div class="col-sm-2 col-md-2">
                <label for="lastName" class="control-label">${msg("licensePlateNumber")}</label>
            </div>

            <div class="col-sm-10 col-md-10">
                <input type="text" class="form-control" id="user.attributes.licensePlateNumber" name="user.attributes.licensePlateNumber" value="${(account.attributes.licensePlateNumber!'')}"/>
            </div>
        </div>

        <div class="form-group ${messagesPerField.printIfExists('locality','has-error')}">
            <div class="col-sm-2 col-md-2">
                <label for="locality" class="control-label">${msg("locality")}</label>
            </div>

            <div class="col-sm-10 col-md-10">
                <input type="search" name="user.attributes.locality" list="alllocations" value="${(account.attributes.locality!'')}"/>
                <datalist id="alllocations">
                 <option value="Solingen">
                 <option value="Berlin">
                 <option value="Hamburg">
                 <option value="Muenchen">
                 <option value="Karlsruhe">
                 <option value="Nuernberg">
                 <option value="Muenster">
                 <option value="Frankfurt">
                 <option value="Stuttgart">
                 <option value="Dortmund">
                 <option value="Novi Sad">
                 <option value="Banja Luka">
                 <option value="Doboj">
                </datalist>
            </div>
        </div>

        <div class="form-group">
            <div id="kc-form-buttons" class="col-md-offset-2 col-md-10 submit">
                <div class="">
                    <#if url.referrerURI??><a href="${url.referrerURI}">${msg("backToApplication")?no_esc}/a></#if>
                    <button type="submit" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}" name="submitAction" value="Save">${msg("doSave")}</button>
                    <button type="submit" class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}" name="submitAction" value="Cancel">${msg("doCancel")}</button>
                </div>
            </div>
        </div>
    </form>

</@layout.mainLayout>
