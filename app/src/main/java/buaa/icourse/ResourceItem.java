package buaa.icourse;


class ResourceItem {
    //资源单项
    private String resourceName;
    private String resourceType;
    private String resourceUrl;
    private String resourceInfo;
    private String resourceUploaderName;
    private int resourceDownloadCount;

    private double evalation;

    ResourceItem(String resourceName, String resourceType, String resourceUrl,
                 String resourceInfo, String resourceUploaderName, int resourceDownloadCount, double evalation
    ) {
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.resourceUrl = resourceUrl;
        this.resourceInfo = resourceInfo;
        this.resourceUploaderName = resourceUploaderName;
        this.resourceDownloadCount = resourceDownloadCount;
        this.evalation = evalation;
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

    double getResourceEvaluation() {
        return this.evalation;
    }

    String getResourceUploaderName() {
        return this.resourceUploaderName;
    }

    int getResourceDownloadCount() {
        return this.resourceDownloadCount;
    }
}
