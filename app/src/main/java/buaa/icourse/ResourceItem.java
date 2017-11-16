package buaa.icourse;


class ResourceItem {
    //资源单项
    private String resourceName;
    private int resouceId;

    ResourceItem(String resourceName, int resouceId) {
        this.resourceName = resourceName;
        this.resouceId = resouceId;
    }

    String getResourceName() {
        return this.resourceName;
    }

    int getResourceId() {
        return this.resouceId;
    }
}
