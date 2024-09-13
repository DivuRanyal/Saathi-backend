<html>
<head>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            border: 1px solid #dddddd;
            border-radius: 10px;
            box-shadow: 0px 0px 10px #dddddd;
        }
        .header {
            background-color: #f4f4f4;
            padding: 10px;
            text-align: center;
            border-bottom: 1px solid #dddddd;
        }
        .header img {
            width: 100px;
            height: auto;
        }
        .content {
            padding: 20px;
        }
        .content h2 {
            color: #333;
        }
        .content p {
            font-size: 16px;
            color: #555;
            line-height: 1.6;
        }
        .footer {
            text-align: center;
            font-size: 12px;
            color: #888;
            padding-top: 20px;
        }
        .saathi-details {
            margin-top: 20px;
            border: 1px solid #dddddd;
            padding: 15px;
            background-color: #f9f9f9;
        }
        .saathi-details h3 {
            margin-bottom: 10px;
            color: #444;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <img src="https://example.com/logo.png" alt="Company Logo">
        </div>
        <div class="content">
            <h2>Hello ${subscriberName},</h2>
            <p>We are pleased to inform you that your account has been activated, and a Saathi (companion) has been assigned to support  your family members.</p>
            <p>Below are the details of your assigned Saathi:</p>
            <div class="saathi-details">
                <h3>Saathi Details</h3>
                <p><strong>Name:</strong> ${saathiName}</p>
                <p><strong>Contact Number:</strong> ${saathiPhone}</p>
                <p><strong>Email:</strong> ${saathiEmail}</p>
                
            </div>
            <p>If you have any questions or need further assistance, feel free to contact us.</p>
            <p>Thank you for choosing our services.</p>
        </div>
        <div class="footer">
            <p>© 2024 Saathi Services. All rights reserved.</p>
        </div>
    </div>
</body>
</html>
