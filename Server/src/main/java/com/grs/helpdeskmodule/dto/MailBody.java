package com.grs.helpdeskmodule.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailBody {

    private String to;
    private String subject;
    private String body;

    public static String sendIssueSubmittedEmail(String username, String trackingNumber) {
        return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 0;
                    padding: 0;
                    background-color: #f4f4f4;
                }
                .email-container {
                    background-color: #ffffff;
                    max-width: 600px;
                    margin: 20px auto;
                    padding: 20px;
                    border-radius: 8px;
                    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                }
                .header {
                    background-color: #007bff;
                    color: #ffffff;
                    text-align: center;
                    padding: 10px;
                    border-radius: 8px 8px 0 0;
                }
                .header h1 {
                    margin: 0;
                    font-size: 20px;
                }
                .content {
                    padding: 20px;
                    line-height: 1.6;
                    color: #333333;
                }
                .footer {
                    text-align: center;
                    font-size: 12px;
                    color: #777777;
                    margin-top: 20px;
                }
                .tracking-number {
                    color: #007bff;
                    font-weight: bold;
                }
            </style>
        </head>
        <body>
            <div class="email-container">
                <div class="header">
                    <h1>ইসু জমাদানের নিশ্চিতকরণ</h1>
                </div>
                <div class="content">
                    <p>প্রিয় %s,</p>
                        <p>আপনার ইসু জমা দেওয়ার জন্য ধন্যবাদ! আমরা এটি সফলভাবে গ্রহণ করেছি এবং আপনার জমাদানের জন্য নিম্নলিখিত ট্র্যাকিং নম্বর বরাদ্দ করেছি:</p>
                        <p class="tracking-number">%s</p>
                        <p>আপনি এই ট্র্যাকিং নম্বর ব্যবহার করে আমাদের প্ল্যাটফর্মে আপনার সমস্যার অবস্থা চেক করতে পারেন।</p>
                        <p>যদি আপনার কোনো প্রশ্ন থাকে, আমাদের টেকনিকাল সাপোর্ট টিমের সাথে যোগাযোগ করতে দ্বিধা করবেন না।</p>
                        <p>শুভেচ্ছান্তে,</p>
                    <p><strong>টেকনিকাল সাপোর্ট টিম</strong></p>
                </div>
                <div class="footer">
                    <p>&copy; 2025 ARC Bangladesh. All rights reserved.</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(username, trackingNumber);
    }
}
