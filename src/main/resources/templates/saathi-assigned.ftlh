<html>
<body>
    <p>Dear ${saathiName},</p>
    <p>We are pleased to inform you that you have been assigned a new subscriber as part of the Saathi service.</p>

    <p><strong>Subscriber Details:</strong></p>
    <ul>
        <li><strong>Name:</strong> ${subscriberName}</li>
        <li><strong>Phone Number:</strong> ${subscriberPhone}</li>
        <li><strong>Email:</strong> ${subscriberEmail}</li>
        <li><strong>Subscription Package:</strong> ${subscriptionPackage}</li>
    </ul>

    <#if patronName??>
        <p><strong>Patron Details (if applicable):</strong></p>
        <ul>
            <li><strong>Name:</strong> ${patronName}</li>
            <li><strong>Relationship:</strong> ${patronRelationship}</li>
            <li><strong>Address:</strong> ${patronAddress}</li>
        </ul>
    </#if>

    <p>Your role is crucial in ensuring the well-being and happiness of our patrons. Please review the details and reach out to the subscriber to introduce yourself.</p>

    <p>Thank you for your commitment to providing excellent service. We are confident that you will make a positive impact on the lives of our patrons and their families.</p>

    <p>Warm regards,<br/>The Saathi Team</p>
</body>
</html>
