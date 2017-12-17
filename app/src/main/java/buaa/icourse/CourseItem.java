package buaa.icourse;


class CourseItem {
    //资源单项
    private String courseName;
    private int courseType;
    private String courseCode;

    CourseItem(String courseName, String courseCode, int courseType) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseType = courseType;
    }

    String getCourseName() {
        return this.courseName;
    }

    String getCourseCode() {
        return this.courseCode;
    }

    int getCourseType() {
        return this.courseType;
    }
}
