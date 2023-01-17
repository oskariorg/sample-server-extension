<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>${viewName}</title>

    <!-- ############# css ################# -->
    <link
            rel="stylesheet"
            type="text/css"
            href="${clientDomain}/Oskari${path}/icons.css"/>
    <link
            rel="stylesheet"
            type="text/css"
            href="${clientDomain}/Oskari${path}/oskari.min.css"/>
    <style type="text/css">
        @media screen {
            body {
                margin: 0;
                padding: 0px;
                height: 100vh;
                width: 100%;
                display: flex;
                flex-direction: column;
            }
            header.serviceheader {
                width: 100%;
                /* Having min and max != normal height */
                min-height: 50px;
                max-height: 50px;
                background-color: crimson;
                color: aliceblue;
                display:block;
                overflow: hidden;
            }
            header.serviceheader h1 {
                margin-left: 15px;
                margin-top: 8px;
                display: inline-block;
            }
            main {
                width: 100%;
                height: 100%;
            }
            main > div.wrapper {
                display: flex;
                width: 100%;
                height: 100%;
            }
            div.sidebar {
                display:block;
                overflow: hidden;
                min-width: 50px;
                max-height: 100%;
                background-color: crimson;
                writing-mode: vertical-rl;
                text-orientation: upright;

                font-size: 20pt;
                padding-right: 14px;
                padding-top: 10px;
            }
            div.sidebar img {
                z-index: 1000;
                position: absolute;
                bottom: 45px;
                left: -50px;
                width: 146px;
                transform: rotate(-90deg);
           }
            div.mappart {
                display: block;
                max-height: 100%;
                width: 100%;
                overflow: hidden;
            }

            #login {
                float: right;
                margin: 8px;
            }

            #login input[type="text"], #login input[type="password"] {
                display: inline-block;
                margin-bottom: 5px;
                background-image: url("${clientDomain}/Oskari/${version}/resources/images/forms/input_shadow.png");
                background-repeat: no-repeat;
                padding-left: 5px;
                padding-right: 5px;
                border: 1px solid #B7B7B7;
                border-radius: 4px 4px 4px 4px;
                box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1) inset;
                color: #878787;
                font: 13px/100% Arial,sans-serif;
            }
            #login input[type="submit"] {
                display: inline-block;
                margin-bottom: 5px;
                padding-left: 5px;
                padding-right: 5px;
                border: 1px solid #B7B7B7;
                border-radius: 4px 4px 4px 4px;
                box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1) inset;
                color: #878787;
                font: 13px/100% Arial,sans-serif;
            }
            #login p.error {
                font-weight: bold;
                color : yellow;
                margin-bottom: 10px;
            }

            #login a {
                color: aliceblue;
                padding: 5px;
            }

            #demolink select {
                max-width: 85%;
                margin: 10px;
            }
        }
    </style>
    <!-- ############# /css ################# -->
</head>
<body>
<header class="serviceheader">
    <h1>Palvelu X</h1>

    <div id="login">
        <c:choose>
            <c:when test="${!empty loginState}">
                <p class="error"><spring:message code="invalid_password_or_username" text="Invalid password or username!" /></p>
            </c:when>
        </c:choose>
        <c:choose>
            <%-- If logout url is present - so logout link --%>
            <c:when test="${!empty _logout_uri}">
                <form action="${pageContext.request.contextPath}${_logout_uri}" method="POST" id="logoutform">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <a href="${pageContext.request.contextPath}${_logout_uri}" style="color: #000000;" onClick="jQuery('#logoutform').submit();return false;"><spring:message code="logout" text="Logout" /></a>
                </form>
                <%-- oskari-profile-link id is used by the personaldata bundle - do not modify --%>
                <a href="${pageContext.request.contextPath}${_registration_uri}" style="color: #000000;" id="oskari-profile-link"><spring:message code="account" text="Account" /></a>
            </c:when>
            <%-- Otherwise show appropriate logins --%>
            <c:otherwise>
                <c:if test="${!empty _login_uri && !empty _login_field_user}">
                    <form action='${pageContext.request.contextPath}${_login_uri}' method="post" accept-charset="UTF-8">
                        <input size="16" id="username" name="${_login_field_user}" type="text" placeholder="<spring:message code="username" text="Username" />" autofocus
                               required>
                        <input size="16" id="password" name="${_login_field_pass}" type="password" placeholder="<spring:message code="password" text="Password" />" required>
                        <input type="submit" id="submit" value="<spring:message code="login" text="Log in" />">
                    </form>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
</header>
<main>
    <div class="wrapper">
        <div class="sidebar">
            <img src="${clientDomain}/Oskari${path}/oskari_rgb_72.png" />
        </div>
        <div class="mappart">
        <%-- Oskari uses an element with id="oskari" as it's root element by default --%>
            <div id="oskari">
                <nav id="maptools">
                    <div id="menubar">
                    </div>
                    <div id="divider">
                    </div>
                    <div id="toolbar">
                    </div>
                     <div id="demolink">
                        <a href="#" style="margin: 10px; color: #ffd400;"
                        onClick="changeAppsetup()">EPSG:3067</a>
                    </div>

                </nav>
            </div>
        </div>
    </div>
</main>

<!-- ############# Javascript ################# -->

<script>
var otherSetup = 'Finland'
function changeAppsetup(parUuid) {
    var uuid = parUuid;
    if (!uuid) {
        var appsetup = uuid || Oskari.app.getSystemDefaultViews().filter(function (appsetup) {
            return  appsetup.name === otherSetup;
        });
        uuid = appsetup[0].uuid;
    }

    window.location=window.location.pathname + '?uuid=' + uuid;
    return false;
}
</script>
<!--  OSKARI -->

<script type="text/javascript">
    var ajaxUrl = '${ajaxUrl}';
    var controlParams = ${controlParams};
</script>
<%-- Pre-compiled application JS, empty unless created by build job --%>
<script type="text/javascript"
        src="${clientDomain}/Oskari${path}/oskari.min.js">
</script>
<%--language files --%>
<script type="text/javascript"
        src="${clientDomain}/Oskari${path}/oskari_lang_${language}.js">
</script>

<script type="text/javascript"
        src="${clientDomain}/Oskari${path}/index.js">
</script>
<script type="text/javascript">
Oskari.on("app.start", function () {
    var container = jQuery('#demolink');
    container.empty();
    container.append('<select>')
    var select = container.find("select");

    select.on("change", function() {
        changeAppsetup(select.val());
    });
    var current = Oskari.app.getUuid();
    Oskari.app.getSystemDefaultViews().forEach(function(item) {
        var opt = jQuery('<option value="' + item.uuid + '">' + item.name + '</option>');
        opt.attr('selected', current === item.uuid);
        select.append(opt);
    });
});
</script>


<!-- ############# /Javascript ################# -->
</body>
</html>
