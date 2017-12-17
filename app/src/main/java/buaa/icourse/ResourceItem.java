package buaa.icourse;


class ResourceItem {
    //资源单项
    private String resourceName;
    private String resourceType;
    private String resourceUrl;
    private String resourceInfo;
    private String resourceUploaderName;
    private int resourceDownloadCount;

    ResourceItem(String resourceName, String resourceType, String resourceUrl,
                 String resourceInfo, String resourceUploaderName, int resourceDownloadCount
    ) {
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.resourceUrl = resourceUrl;
        this.resourceInfo = resourceInfo;
        this.resourceUploaderName = resourceUploaderName;
        this.resourceDownloadCount = resourceDownloadCount;
    }

    String getResourceName() {
        return this.resourceName;
    }

    String getResourceType() {
        return this.resourceType;
    }

    String getResourceUrl() {
        return this.resourceUrl;
    }

    String getResourceInfo() {
        return this.resourceInfo;
    }

    String getResourceUploaderName() {
        return this.resourceUploaderName;
    }

    int getResourceDownloadCount() {
        return this.resourceDownloadCount;
    }
}
