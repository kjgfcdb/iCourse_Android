package buaa.icourse;



public class ResourceItem {
    private String resourceName;
    private int resouceId;
    public ResourceItem(String resourceName,int resouceId) {
        this.resourceName = resourceName;
        this.resouceId = resouceId;
    }
    String getResourceName() {
        return this.resourceName;
    }
    public int getResourceId() {
        return this.resouceId;
    }
}
