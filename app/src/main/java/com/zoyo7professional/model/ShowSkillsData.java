package com.zoyo7professional.model;

public class ShowSkillsData {
    private String SkillName="";
    private String SkillId="";

    public String getSkillId() {
        return SkillId;
    }

    public void setSkillId(String skillId) {
        SkillId = skillId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    private String Status="";

    public String getSkillName() {
        return SkillName;
    }

    public void setSkillName(String skillName) {
        SkillName = skillName;
    }
}
