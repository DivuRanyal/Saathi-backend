<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome to Saathi - Account Created</title>
</head>
<body>
    <div style="font-family: Arial, sans-serif; color: #333; line-height: 1.6;">
        <h2 style="color: #5A5A5A;">Hello ${saathiFirstName}<#if saathiLastName?? && (saathiLastName != "")> ${saathiLastName}</#if>,</h2>

        <p>We are excited to welcome you to the Saathi team! Your account has been successfully created. Below are your credentials for accessing the Saathi Dashboard:</p>

        <table style="border-collapse: collapse; width: 100%; margin-top: 20px;">
            <tr>
                <td style="padding: 10px; border: 1px solid #ccc;"><strong>Email</strong></td>
                <td style="padding: 10px; border: 1px solid #ccc;">${saathiEmail}</td>
            </tr>
            <tr>
                <td style="padding: 10px; border: 1px solid #ccc;"><strong>Password</strong></td>
                <td style="padding: 10px; border: 1px solid #ccc;">${saathiPassword}</td>
            </tr>
        </table>

        <p>You can access the Saathi Dashboard using the following link:</p>
        <p><a href="${saathiLoginLink}" style="color: #4CAF50; text-decoration: none;">Go to Saathi Dashboard</a></p>

        <h3>Important:</h3>
        <p>For security reasons, we strongly recommend that you log in and change your password immediately after accessing the dashboard.</p>

        <p>If you have any questions or need assistance, feel free to contact us at <a href="mailto:support@saathi.com">support@saathi.com</a>.</p>

        <p>Best Regards,</p>
        <p>The Saathi Team</p>
    </div>
</body>
</html>
