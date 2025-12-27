package com.propadda.prop.service;

public final class PropertyExpiryEmailTemplate {

    private PropertyExpiryEmailTemplate() {}

    /* -------------------------------------------------
       1️⃣ EXPIRY REMINDER (NO RENEW CTA)
       ------------------------------------------------- */
    public static String buildReminderEmail(
            String userName,
            String propertyTitle,
            int daysLeft
    ) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <style>
                body {
                    margin: 0;
                    padding: 0;
                    background-color: #f4f6f8;
                    font-family: Arial, Helvetica, sans-serif;
                }
                .container {
                    max-width: 600px;
                    margin: 30px auto;
                    background: #ffffff;
                    padding: 24px;
                    border-radius: 8px;
                }
                .header {
                    font-size: 20px;
                    font-weight: bold;
                    color: #222;
                    margin-bottom: 12px;
                }
                .text {
                    font-size: 14px;
                    color: #444;
                    line-height: 1.6;
                }
                .highlight {
                    font-weight: bold;
                    color: #e65100;
                }
                .footer {
                    margin-top: 30px;
                    font-size: 12px;
                    color: #777;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    Property Expiry Reminder
                </div>

                <div class="text">
                    Hi %s,<br><br>

                    Your property listing
                    <span class="highlight">"%s"</span>
                    will expire in
                    <span class="highlight">%d day%s</span>.
                    <br><br>

                    This is just an advance intimation to help you plan ahead.
                    No action is required at this moment.
                </div>

                <div class="footer">
                    This is an automated reminder from PropAdda.<br>
                    Please do not reply to this email.
                </div>
            </div>
        </body>
        </html>
        """.formatted(
                userName != null ? userName : "User",
                propertyTitle,
                daysLeft,
                daysLeft == 1 ? "" : "s"
        );
    }

    /* -------------------------------------------------
       2️⃣ PROPERTY EXPIRED (WITH RENEW CTA)
       ------------------------------------------------- */
    public static String buildExpiredEmail(
            String userName,
            String propertyTitle
    ) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <style>
                body {
                    margin: 0;
                    padding: 0;
                    background-color: #f4f6f8;
                    font-family: Arial, Helvetica, sans-serif;
                }
                .container {
                    max-width: 600px;
                    margin: 30px auto;
                    background: #ffffff;
                    padding: 24px;
                    border-radius: 8px;
                }
                .header {
                    font-size: 20px;
                    font-weight: bold;
                    color: #b71c1c;
                    margin-bottom: 12px;
                }
                .text {
                    font-size: 14px;
                    color: #444;
                    line-height: 1.6;
                }
                .highlight {
                    font-weight: bold;
                }
                .cta {
                    display: inline-block;
                    margin-top: 20px;
                    padding: 12px 20px;
                    background-color: #ff6f00;
                    color: #ffffff !important;
                    text-decoration: none;
                    border-radius: 4px;
                    font-size: 14px;
                    font-weight: bold;
                }
                .footer {
                    margin-top: 30px;
                    font-size: 12px;
                    color: #777;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    Property Listing Expired
                </div>

                <div class="text">
                    Hi %s,<br><br>

                    Your property listing
                    <span class="highlight">"%s"</span>
                    has expired and is no longer visible to users.
                    <br><br>

                    You can renew the listing to make it active again
                    and start receiving enquiries.
                </div>

                <a href="https://propadda.in/agent/listings/expired"
                   class="cta">
                    Renew Listing
                </a>

                <div class="footer">
                    This is an automated notification from PropAdda.<br>
                    Please do not reply to this email.
                </div>
            </div>
        </body>
        </html>
        """.formatted(
                userName != null ? userName : "User",
                propertyTitle
        );
    }
}
