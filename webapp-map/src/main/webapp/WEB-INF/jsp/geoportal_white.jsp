<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="format-detection" content="telephone=no" />
    <%-- For overriding the default favicon you can do this or provide an alternative favicon.ico on your application --%>
    <%-- link rel="icon" type="image/png" href="${ajaxUrl}action_route=Logo" --%>
    <!-- Default favicon (for browsers that don't support media queries in link tags) -->
    <link rel="icon" href="favicon.ico" type="image/x-icon">

    <!-- Light mode favicon -->
    <link rel="icon" href="favicon-light.ico" type="image/x-icon" media="(prefers-color-scheme: light)">

    <!-- Dark mode favicon -->
    <link rel="icon" href="favicon-dark.ico" type="image/x-icon" media="(prefers-color-scheme: dark)">
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
            .oskari-tile {
                background: #A3C4BC !important;
                border-bottom: none !important;
                border-radius: 5px;
                margin: 3px;
                max-width: 98%;
                box-shadow: 1px 1px 2px 0px rgba(0, 0, 0, 0.5);
            }
            .oskari-tile-closed {
                /* background-color: white !important;*/
                border-top-width: 0px !important;
            }

            .oskari-tile-attached {
                background-color: #114B5F !important;
                border-bottom: none !important;
                border-top: none !important;
            }

            .oskari-tile-attached .oskari-tile-title {
                color: white !important;
            }

            div.toolbar_default div.toolrow div.tool:hover:not(.disabled):not(.selected) {
                background-color: #114B5F !important;
            }

            div.toolrow div.tool.selected, div.toolrow div.tool:active:not(.disabled) {
                background-color: #114B5F !important;
            }

            .oskari-tile-closed:hover {
                border-top: none !important;
                background: linear-gradient(rgb(48, 116, 115), rgb(17, 75, 95)) !important;
            }
            .oskari-tile-closed .oskari-tile-title {
                color: black !important;
            }

            #toolbar {
                margin: 30px 5px 30px 5px !important;
                width: auto !important;
                border: 1px black solid;
                border-radius: 5px;
                box-shadow: 1px 1px 2px 0px rgba(0, 0, 0, 0.5);
            }

            #toolbar div.toolrow {
                border: 0px;
            }

            #logobar {
                background-image: url('${clientDomain}/Oskari${path}/oskari_logo_rgb_horizontal.svg') !important;
            }

            /* Override the "open menu style" */
            .oskari-tile-attached.statsgrid {
                background-color: #114B5F !important;
                color: white !important;
            }
            .oskari-tile.oskari-tile-attached.statsgrid .statsgrid-functionality .text {
                color: white !important;

            }

            #login {
                margin-left: 5px;
            }

            #login input[type="text"], #login input[type="password"] {
                width: 90%;
                margin-bottom: 5px;
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
                width: 90%;
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
                color : red;
                margin-bottom: 10px;
            }

            #login a {
                color: #FFF;
                padding: 5px;
            }

            nav#maptools select {
                max-width: 85%;
                margin: 10px;
            }
        }
        @media only screen (min-width: 768px) {
            #login input {
                font: 16px/100% Arial,sans-serif;
            }
        }
    </style>
    <!-- ############# /css ################# -->
</head>
<%-- Not defining an element with id="oskari" will make Oskari use the body-tag as root element --%>
<body>
<%--
Normally we would let frontend code to create the map container,
but since we want to force it to appear before the navigation we declare it here.
--%>
<div id="contentMap">
    <div id="mapdiv"></div>
</div>
<nav id="maptools">
    <div id="logobar">
    </div>
    <div id="menubar">
    </div>
    <div id="divider">
    </div>
    <div id="toolbar">
    </div>
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
                    <a href="${pageContext.request.contextPath}${_logout_uri}" style="color: #000000;" onClick="jQuery('#logoutform').submit();return false;"><spring:message code="logout" text="Logout" /></a>
                </form>
                <%-- oskari-profile-link id is used by the personaldata bundle - do not modify --%>
                <a href="${pageContext.request.contextPath}${_registration_uri}" style="color: #000000;" id="oskari-profile-link"><spring:message code="account" text="Account" /></a>
            </c:when>
            <%-- Otherwise show appropriate logins --%>
            <c:otherwise>
                <c:if test="${!empty _login_uri && !empty _login_field_user}">
                    <form action='${pageContext.request.contextPath}${_login_uri}' method="post" accept-charset="UTF-8">
                        <input size="16" id="username" name="${_login_field_user}" type="text" autocomplete="username" placeholder="<spring:message code="username" text="Username" />" autofocus
                               required>
                        <input size="16" id="password" name="${_login_field_pass}" type="password" autocomplete="current-password" placeholder="<spring:message code="password" text="Password" />" required>
                        <input type="submit" id="submit" value="<spring:message code="login" text="Log in" />">
                    </form>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
    <div id="demolink">
        <a href="#" style="margin: 10px; color: #3c3c3c;"
        onClick="changeAppsetup()">EPSG:3857</a>
    </div>
    <div id="language-selector-root"></div>
</nav>

<!-- ############# Javascript ################# -->

<script>
var otherSetup = 'World'
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
    container.append('<select title="Select application">')
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
