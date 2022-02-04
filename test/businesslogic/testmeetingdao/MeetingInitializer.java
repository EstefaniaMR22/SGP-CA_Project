/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package businesslogic.testmeetingdao;

import sgp.ca.businesslogic.MeetingDAO;
import sgp.ca.domain.Agreement;
import sgp.ca.domain.AssistantRol;
import sgp.ca.domain.Comment;
import sgp.ca.domain.Meeting;
import sgp.ca.domain.MeetingAgenda;
import sgp.ca.domain.Prerequisite;
import sgp.ca.domain.Topic;

public class MeetingInitializer{
    private final MeetingDAO MEETING_DAO = new MeetingDAO();
    private Meeting meeting;
    public void createObjectForTest(){
        meeting = new Meeting(0, "2021-05-02", "17:00:00", "Plan de Estudios de ISOF", "2021-04-30", "Realizada", "Teams", "Actualizar plan de estudios", "1. Se necesita un plan de trabajo", "1. Realizar plan de trabajo","OFIJO3IJ3OIDJ3O");
        meeting.setMeetingAgenda(new MeetingAgenda (0,"02:04:00", "02:00:00", 2));
        meeting.getMeetingAgenda().addTopic(new Topic(0, "17:00:00", "18:04:00", "01:00:00","01:04:00","Plan de estudios ISOF", "Dra. María Karen Cortés Verdín", "Concluido"));
        meeting.getMeetingAgenda().addTopic(new Topic(0, "18:04:00", "19:04:00", "01:00:00", "01:00:00", "Eventos del Semestres Enero-Julio 2021", "Mtro. Juan Carlos Perez Arriaga", "Concluido"));
        meeting.getMeetingAgenda().addPrerequisite(new Prerequisite(0, "Mtro. Juan Carlos Pérez Arriaga", "Lista de eventos en los que está invitada la FEI"));
        meeting.getMeetingAgenda().addPrerequisite(new Prerequisite(0, "Dr. Jorge Octavio Ocharán Hernández", "Borrador de plan de estudios de ISOF"));
        meeting.getAgreements().add(new Agreement(0, "Reunión con el director de la FEI", "Dra. María Karen Cortés Verdín", "2021-06-01"));
        meeting.getAgreements().add(new Agreement(0, "Reunión con la secretaria académica de la FEI", "Dr. Jorge Octavio Ocharán Hernández", "2021-05-15"));
        meeting.getComments().add(new Comment(0, "Faltaron notas", "Dr. Angel Juan Sanchez Garcia", "20:00:01", "2021-04-30"));
        meeting.getComments().add(new Comment(0, "La fecha de reunion con el director de la FEI es incorrecta", "Dra. Maria Karen Cortes Verdín", "19:32:00", "2021-04-30"));
        meeting.getAssistantsRol().add(new AssistantRol(0, "Angel Juan Sánchez García", "Secretario", 1));
    }
    
    public void preparedRequestMeetingTestCase(){
        if(MEETING_DAO.getMeeting(1).getMeetingProject() == null){
            this.createObjectForTest();
            MEETING_DAO.addMeeting(meeting);
        }
    }
    
    public void cleanMeetingTestCaseData(){
        this.createObjectForTest();
        meeting = MEETING_DAO.getMeeting(1);
        MEETING_DAO.deleteMeeting(meeting);
    }
}
