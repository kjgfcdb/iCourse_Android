package buaa.icourse;


class ResourceItem {
    //资源单项
    private String resourceName;
    private String resourceType;

    ResourceItem(String resourceName, String resourceType) {
        this.resourceName = resourceName;
        this.resourceType = resourceType;
    }

    String getResourceName() {
        return this.resourceName;
    }

    String getResourceType() {
        return this.resourceType;
    }
}
