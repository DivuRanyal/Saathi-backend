<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome to Saathi - Admin Account Created</title>
</head>
<body>
    <div style="font-family: Arial, sans-serif; color: #333; line-height: 1.6;">
        <h2 style="color: #5A5A5A;">Hello ${adminFirstName}<#if adminLastName?? && (adminLastName != "")> ${adminLastName}</#if>,</h2>
        
        <p>We are pleased to inform you that your Admin account has been successfully created. Below are your credentials for accessing the Admin Dashboard:</p>
        
        <table style="border-collapse: collapse; width: 100%; margin-top: 20px;">
            <tr>
                <td style="padding: 10px; border: 1px solid #ccc;"><strong>Email</strong></td>
                <td style="padding: 10px; border: 1px solid #ccc;">${adminEmail}</td>
            </tr>
            <tr>
                <td style="padding: 10px; border: 1px solid #ccc;"><strong>Password</strong></td>
                <td style="padding: 10px; border: 1px solid #ccc;">${adminPassword}</td>
            </tr>
        </table>

        <p>You can access the Admin Dashboard here:</p>
        <p><a href="${adminLoginLink}" style="color: #4CAF50; text-decoration: none;">Go to Admin Dashboard</a></p>

        <p>If you have any questions, feel free to contact us at <a href="mailto:support@saathi.com">support@saathi.com</a>.</p>

        <p>Best Regards,</p>
        <p>The Saathi Team</p>
    </div>
</body>
</html>
