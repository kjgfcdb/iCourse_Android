package buaa.icourse;


class CourseItem {
    //资源单项
    private String courseName;
    private int collegeId;
    private String teacherName;
    private String credit;
    private String courseType;

    public int getCollegeId() {
        return collegeId;
    }

    private String courseCode;

    CourseItem(String courseName, String courseCode, int collegeId,String teacherName,String credit,String courseType) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.collegeId = collegeId;
        this.teacherName = teacherName;
        this.credit = credit;
        this.courseType = courseType;
    }

    String getCourseName() {
        return this.courseName;
    }

    String getCourseCode() {
        return this.courseCode;
    }

    public String getCredit() {
        return credit;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getCourseType() {
        return courseType;
    }
}
