<%--
  Created by IntelliJ IDEA.
  User: Class2020
  Date: 6/4/2020
  Time: 2:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Launch Page</title>
</head>
<body>
    <h1>Launch Page</h1>
    <p>This is the configuration xml for canvas</p>

    <pre>
----------- Start copying the next line -----------
&lt;?xml version="1.0" encoding="UTF-8"?&gt;
&lt;cartridge_basiclti_link xmlns="http://www.imsglobal.org/xsd/imslticc_v1p0"
    xmlns:blti = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0"
    xmlns:lticm ="http://www.imsglobal.org/xsd/imslticm_v1p0"
    xmlns:lticp ="http://www.imsglobal.org/xsd/imslticp_v1p0"
    xmlns:xsi = "http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation = "http://www.imsglobal.org/xsd/imslticc_v1p0 http://www.imsglobal.org/xsd/lti/ltiv1p0/imslticc_v1p0.xsd
    http://www.imsglobal.org/xsd/imsbasiclti_v1p0 http://www.imsglobal.org/xsd/lti/ltiv1p0/imsbasiclti_v1p0.xsd
    http://www.imsglobal.org/xsd/imslticm_v1p0 http://www.imsglobal.org/xsd/lti/ltiv1p0/imslticm_v1p0.xsd
    http://www.imsglobal.org/xsd/imslticp_v1p0 http://www.imsglobal.org/xsd/lti/ltiv1p0/imslticp_v1p0.xsd"&gt;
    &lt;blti:title&gt;Liquiz Grade&lt;/blti:title&gt;
    &lt;blti:description&gt;Liquiz Grade&lt;/blti:description&gt;
    &lt;blti:launch_url&gt;${url}&lt;/blti:launch_url&gt;
    &lt;blti:extensions platform="canvas.instructure.com"&gt;
      &lt;lticm:property name="privacy_level"&gt;public&lt;/lticm:property&gt;
      &lt;lticm:options name="course_navigation"&gt;
        &lt;lticm:property name="enabled"&gt;true&lt;/lticm:property&gt;
        &lt;lticm:property name="visibility"&gt;members&lt;/lticm:property&gt;
      &lt;/lticm:options&gt;
    &lt;/blti:extensions&gt;
&lt;/cartridge_basiclti_link&gt;
----------- Stop. Do not copy this line -----------
    </pre>

</body>
</html>
