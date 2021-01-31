<%--@elvariable id="url" type="java.lang.String"--%>
<%--@elvariable id="domain" type="java.lang.String"--%>
<%@ page contentType="application/xml;charset=UTF-8" language="java" %><?xml version="1.0" encoding="UTF-8"?>
<cartridge_basiclti_link xmlns="http://www.imsglobal.org/xsd/imslticc_v1p3"
                         xmlns:blti="http://www.imsglobal.org/xsd/imsbasiclti_v1p3"
                         xmlns:lticm="http://www.imsglobal.org/xsd/imslticm_v1p3"
                         xmlns:lticp="http://www.imsglobal.org/xsd/imslticp_v1p3"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://www.imsglobal.org/xsd/imslticc_v1p0 http://www.imsglobal.org/xsd/lti/ltiv1p0/imslticc_v1p3.xsd
    http://www.imsglobal.org/xsd/imsbasiclti_v1p0 http://www.imsglobal.org/xsd/lti/ltiv1p0/imsbasiclti_v1p3.xsd
    http://www.imsglobal.org/xsd/imslticm_v1p0 http://www.imsglobal.org/xsd/lti/ltiv1p0/imslticm_v1p3.xsd
    http://www.imsglobal.org/xsd/imslticp_v1p0 http://www.imsglobal.org/xsd/lti/ltiv1p0/imslticp_v1p3.xsd">
    <blti:title>Liquiz Grade</blti:title>
    <blti:description>Liquiz Grade</blti:description>
    <blti:extensions platform="canvas.instructure.com">
        <lticm:property name="tool_id">liquiz</lticm:property>
        <lticm:property name="domain">${domain}</lticm:property>
        <lticm:options name="custom_fields">
            <lticm:property name="canvas_course_name">$Canvas.course.name</lticm:property>
        </lticm:options>
        <lticm:property name="privacy_level">public</lticm:property>
        <lticm:options name="course_navigation">
            <lticm:property name="url">${url}/teacher/launch</lticm:property>>
            <lticm:property name="enabled">true</lticm:property>
            <lticm:property name="visibility">admins</lticm:property>
            <lticm:property name="windowTarget">_blank</lticm:property>
        </lticm:options>
        <lticm:options name="assignment_selection">
            <lticm:property name="url">${url}/student/launch</lticm:property>>
            <lticm:property name="enabled">true</lticm:property>
            <lticm:property name="visibility">members</lticm:property>
        </lticm:options>
        <lticm:options name="assignment_view">
            <lticm:property name="url">${url}/student/launch</lticm:property>>
            <lticm:property name="enabled">true</lticm:property>
            <lticm:property name="visibility">members</lticm:property>
        </lticm:options>
        <lticm:options name="assignment_edit">
            <lticm:property name="url">${url}/student/launch</lticm:property>>
            <lticm:property name="enabled">true</lticm:property>
            <lticm:property name="visibility">members</lticm:property>
            <lticm:options name="custom_fields">
                <lticm:property name="canvas_assignment_id">$Canvas.assignment.id</lticm:property>
            </lticm:options>
        </lticm:options>
    </blti:extensions>
</cartridge_basiclti_link>
