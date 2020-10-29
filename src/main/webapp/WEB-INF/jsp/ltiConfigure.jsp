<%@ page contentType="application/xml;charset=UTF-8" language="java" %><?xml version="1.0" encoding="UTF-8"?>
<cartridge_basiclti_link xmlns="http://www.imsglobal.org/xsd/imslticc_v1p0"
                         xmlns:blti = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0"
                         xmlns:lticm ="http://www.imsglobal.org/xsd/imslticm_v1p0"
                         xmlns:lticp ="http://www.imsglobal.org/xsd/imslticp_v1p0"
                         xmlns:xsi = "http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation = "http://www.imsglobal.org/xsd/imslticc_v1p0 http://www.imsglobal.org/xsd/lti/ltiv1p0/imslticc_v1p0.xsd
    http://www.imsglobal.org/xsd/imsbasiclti_v1p0 http://www.imsglobal.org/xsd/lti/ltiv1p0/imsbasiclti_v1p0.xsd
    http://www.imsglobal.org/xsd/imslticm_v1p0 http://www.imsglobal.org/xsd/lti/ltiv1p0/imslticm_v1p0.xsd
    http://www.imsglobal.org/xsd/imslticp_v1p0 http://www.imsglobal.org/xsd/lti/ltiv1p0/imslticp_v1p0.xsd">
    <blti:title>Liquiz Grade</blti:title>
    <blti:description>Liquiz Grade</blti:description>
    <blti:launch_url>${url}</blti:launch_url>
    <blti:domain>${domain}</blti:domain>
    <blti:extensions platform="canvas.instructure.com">
        <lticm:property name="privacy_level">public</lticm:property>
        <lticm:options name="course_navigation">
            <lticm:property name="enabled">true</lticm:property>
            <lticm:property name="visibility">admins</lticm:property>
        </lticm:options>
        <lticm:options name="assignment_view">
            <blti:launch_url>${url}/quizResult?qID=xyz</blti:launch_url>
            <lticm:property name="enabled">true</lticm:property>
            <lticm:property name="visibility">members</lticm:property>
        </lticm:options>
    </blti:extensions>
</cartridge_basiclti_link>
