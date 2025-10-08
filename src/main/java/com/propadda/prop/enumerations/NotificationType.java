package com.propadda.prop.enumerations;

public enum NotificationType {
    ListingAcknowledgement, //for Buyer: Thanks! Your property ‘{Title}’ was received and is pending approval.
    ListingApproval, //for Seller: Great news! ‘{Title}’ is live. View.
    ListingRejection, //for Seller: {Title}’ was not approved for: {Reason}. Please edit & resubmit.
    BuyerAcknowledgement, //for Buyer: We’ve received your enquiry for ‘{Title}’. Our team will contact you shortly.
    EnquiryForAdmin, //for Admin from Buyer: New enquiry on ‘{Title}’ from {Name}/{Mobile}. Please route.
    ListingApprovalRequest, //for Admin from Seller: New Property added. Approve/Reject
    KycApprovalRequest, //for Admin from Seller: User added KYC details. Approve/Reject
    KycApproved, //for Seller: Great news! KYC approved. Add properties now.
    KycRejected, //for Seller: KYC was not approved for: {Reason}. Please edit & resubmit.
    ExpiredListing, //for Seller: {Title} has expired. Please renew from Admin panel.
    RenewedListing, //for Seller: {Title} has been renewed.
    ExpiryReminder //{Title}’ expires in {X} days. Renew now.
}
