package buaa.icourse;


class CollegeItem {
    //院系单项,获取院系名称或者院系代号
    private String mCollegeName;

    public String getCollegeName() {
        return mCollegeName;
    }

    public int getCollegeId() {
        return mCollegeId;
    }

    private int mCollegeId;

    CollegeItem(String collegeName,int collegeId) {
        mCollegeName = collegeName;
        mCollegeId = collegeId;
    }


}
