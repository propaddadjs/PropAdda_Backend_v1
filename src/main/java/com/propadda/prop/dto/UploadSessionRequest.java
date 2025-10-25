// Author-Hemant Arora
package com.propadda.prop.dto;

import java.util.List;

public class UploadSessionRequest {
    public List<UploadSessionFileRequest> files;
    public Integer userId; // optional
}