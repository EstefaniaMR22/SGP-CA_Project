package model.domain;

import java.util.Date;

import java.util.List;

    public class Project {

        private int idProject;
        private String projectName;
        private int idLGCA;
        private String status;
        private Date startDate;
        private Date endDate;
        private Date estimatedEndDate;
        private String startDateString;
        private String endDateString;
        private String estimatedEndDateString;
        private String description;
        private int durationProjectInMonths;
        private List<Evidence> evidence;

        public Project() {
        }

        public String getProjectName(){
            return projectName;
        }

        public void setProjectName(String projectName){
            this.projectName = projectName;
        }

        public String getDescription(){
            return description;
        }

        public void setDescription(String description){
            this.description = description;
        }

        public int getDurationProjectInMonths(){
            return durationProjectInMonths;
        }

        public void setDurationProjectInMonths(int durationProjectInMonths){
            this.durationProjectInMonths = durationProjectInMonths;
        }

        public String getStatus(){
            return status;
        }

        public void setStatus(String status){
            this.status = status;
        }


        public Evidence getEvidenceByUrlFile(String urlFile){
            Evidence evidenceReturn = null;
            for(Evidence evidence : this.evidence){
                if(evidence.getUrlFile() == urlFile){
                    evidenceReturn = evidence;
                }
            }
            return evidenceReturn;
        }

        public List<Evidence> getEvidences(){
            return evidence;
        }

        public void setEvidence(List<Evidence> evidence) {
            this.evidence = evidence;
        }

        public int getIdLGCA() {
            return idLGCA;
        }

        public void setIdLGCA(int idLGCA) {
            this.idLGCA = idLGCA;
        }

        public int getIdProject() {
            return idProject;
        }

        public void setIdProject(int idProject) {
            this.idProject = idProject;
        }


        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public Date getEstimatedEndDate() {
            return estimatedEndDate;
        }

        public void setEstimatedEndDate(Date estimatedEndDate) {
            this.estimatedEndDate = estimatedEndDate;
        }

        public String getStartDateString() {
            return startDateString;
        }

        public void setStartDateString(String startDateString) {
            this.startDateString = startDateString;
        }

        public String getEndDateString() {
            return endDateString;
        }

        public void setEndDateString(String endDateString) {
            this.endDateString = endDateString;
        }

        public String getEstimatedEndDateString() {
            return estimatedEndDateString;
        }

        public void setEstimatedEndDateString(String estimatedEndDateString) {
            this.estimatedEndDateString = estimatedEndDateString;
        }
    }

