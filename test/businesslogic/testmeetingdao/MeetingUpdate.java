/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package businesslogic.testmeetingdao;

import org.junit.Assert;
import org.junit.Test;
import sgp.ca.businesslogic.MeetingDAO;
import sgp.ca.domain.Agreement;
import sgp.ca.domain.AssistantRol;
import sgp.ca.domain.Comment;
import sgp.ca.domain.Meeting;
import sgp.ca.domain.MeetingAgenda;
import sgp.ca.domain.Prerequisite;
import sgp.ca.domain.Topic;

public class MeetingUpdate{
    public final MeetingInitializer INITIALIZER = new MeetingInitializer();
    public final MeetingDAO MEETING_DAO = new MeetingDAO();
    
    
    @Test
    public void testCorrectMeetingUpdateDifferentTopics(){
        INITIALIZER.preparedRequestMeetingTestCase();
        Meeting oldMeeting = MEETING_DAO.getMeeting(2);
        Meeting newMeeting = new Meeting(0, "2021-05-03", "13:00:00", "Plan de Estudios de ISOF 2021", "2021-04-30", "Realizada", "Teams", "Asunto de la reunión", "1. Se necesita un plan de trabajo", "1. Realizar plan de trabajo", "OFIJO3IJ3OIDJ3O");
        newMeeting.setMeetingAgenda(oldMeeting.getMeetingAgenda());
        newMeeting.getMeetingAgenda().getTopics().add(new Topic(0, "17:00:00", "18:10:00", "01:00:00","01:10:00","Plan de estudios ISOF", "Dra. María Karen Cortés Verdín", "Concluido"));
        newMeeting.getMeetingAgenda().getTopics().add(new Topic(0, "18:10:00", "19:10:00", "01:00:00", "01:00:00", "Eventos del Semestres Enero-Julio 2021", "Dr. Angel Juan Sanchez Garcia", "Concluido"));
        newMeeting.getMeetingAgenda().setPrerequisites(oldMeeting.getMeetingAgenda().getPrerequisites());
        newMeeting.setAgreements(oldMeeting.getAgreements());
        newMeeting.setComments(oldMeeting.getComments());
        newMeeting.setAssistantsRol(oldMeeting.getAssistantsRol());
        MEETING_DAO.updateMeeting(newMeeting, oldMeeting);
        Meeting meetingRetrieved = MEETING_DAO.getMeeting(2);
        MEETING_DAO.deleteMeeting(newMeeting);
        Assert.assertNotEquals(oldMeeting.getMeetingAgenda().getTopics(), meetingRetrieved.getMeetingAgenda().getTopics());
    }
    
    @Test
    public void testCorrectMeetingUpdateDifferentPrerequisie(){
        INITIALIZER.preparedRequestMeetingTestCase();
        Meeting oldMeeting = MEETING_DAO.getMeeting(3);
        Meeting newMeeting = new Meeting(0, "2021-05-02", "17:00:00", "Plan de Estudios de ISOF", "2021-04-30", "Realizada", "Teams", "Asunto de la reunion", "1. Se necesita un plan de trabajo", "1. Realizar plan de trabajo", "OFIJO3IJ3OIDJ3O");
        newMeeting.setMeetingAgenda(oldMeeting.getMeetingAgenda());
        newMeeting.getMeetingAgenda().setTopics(oldMeeting.getMeetingAgenda().getTopics());
        newMeeting.getMeetingAgenda().addPrerequisite(new Prerequisite(0, "Mtro. Xavier Limon Riaño", "Resporte de disponibilidad de efectivo para los eventos"));
        newMeeting.getMeetingAgenda().addPrerequisite(new Prerequisite(0, "Dr. Jorge Octavio Ocharán Hernández", "Planes de estudios pasados de la ISOF"));
        newMeeting.getMeetingAgenda().addPrerequisite(new Prerequisite(0, "Dr. Dr. Angel Juan Sanchez Garcia", "Resporte de los trabajos recepcionales concluidos en el mes de Abril"));
        newMeeting.setAgreements(oldMeeting.getAgreements());
        newMeeting.setComments(oldMeeting.getComments());
        newMeeting.setAssistantsRol(oldMeeting.getAssistantsRol());
        MEETING_DAO.updateMeeting(newMeeting, oldMeeting);
        Meeting meetingRetrieved = MEETING_DAO.getMeeting(3);
        MEETING_DAO.deleteMeeting(newMeeting);
        Assert.assertNotEquals(oldMeeting.getMeetingAgenda().getPrerequisites(), meetingRetrieved.getMeetingAgenda().getPrerequisites());
    }
    
    @Test
    public void testCorrectMeetingUpdateDifferentAgendaMeeting(){
        INITIALIZER.preparedRequestMeetingTestCase();
        Meeting oldMeeting = MEETING_DAO.getMeeting(1);
        Meeting newMeeting = new Meeting(0, "2021-05-02", "17:00:00", "Plan de Estudios de ISOF", "2021-04-30", "Realizada", "Teams", "Asunto de la reunión", "1. Se necesita un plan de trabajo", "1. Realizar plan de trabajo", "OFIJO3IJ3OIDJ3O");
        newMeeting.setMeetingAgenda(new MeetingAgenda(0, "01:30:00","01:30:00", 5));
        newMeeting.getMeetingAgenda().getTopics().add(new Topic(0, "17:00:00", "18:10:00", "01:00:00","01:10:00","Plan de estudios ISOF", "Dra. María Karen Cortés Verdín", "Concluido"));
        newMeeting.getMeetingAgenda().getTopics().add(new Topic(0, "18:10:00", "18:30:00", "00:30:00", "00:20:00", "Eventos del Semestres Enero-Julio 2021", "Dr. Angel Juan Sanchez Garcia", "Concluido"));
        newMeeting.getMeetingAgenda().setPrerequisites(oldMeeting.getMeetingAgenda().getPrerequisites());
        newMeeting.setAgreements(oldMeeting.getAgreements());
        newMeeting.setComments(oldMeeting.getComments());
        newMeeting.setAssistantsRol(oldMeeting.getAssistantsRol());
        MEETING_DAO.updateMeeting(newMeeting, oldMeeting);
        Meeting meetingRetrieved = MEETING_DAO.getMeeting(1);
        MEETING_DAO.deleteMeeting(newMeeting);
        Assert.assertNotEquals(oldMeeting.getMeetingAgenda(), meetingRetrieved.getMeetingAgenda());
    }
    
    @Test
    public void testCorrectMeetingUpdateDifferetAgreements(){
        INITIALIZER.preparedRequestMeetingTestCase();
        Meeting oldMeeting = MEETING_DAO.getMeeting(1);
        Meeting newMeeting = new Meeting(0, "2021-05-02", "17:00:00", "Plan de Estudios de ISOF", "2021-04-30", "Realizada", "Teams", "Asunto de la reunión","1. Se necesita un plan de trabajo", "1. Realizar plan de trabajo", "OFIJO3IJ3OIDJ3O");
        newMeeting.setMeetingAgenda(oldMeeting.getMeetingAgenda());
        newMeeting.getMeetingAgenda().setTopics(oldMeeting.getMeetingAgenda().getTopics());
        newMeeting.getMeetingAgenda().setPrerequisites(oldMeeting.getMeetingAgenda().getPrerequisites());
        newMeeting.getAgreements().add(new Agreement(0, "Reunión con el director de la FEI y secretaroa académica", "Dra. María Karen Cortés Verdín", "2021-06-01"));
        newMeeting.getAgreements().add(new Agreement(0, "Nuevo plan de estudios de la ISOP en limpio", "Dr. Jorge Octavio Ocharán Hernández", "2021-05-10"));
        newMeeting.getAgreements().add(new Agreement(0, "Resporte de actividades para el primer evento del semestre Agosto-Diciembre 2021", "Mtro. Juan Carlod Pérez Arriaga", "2021-05-15"));
        newMeeting.setComments(oldMeeting.getComments());
        newMeeting.setAssistantsRol(oldMeeting.getAssistantsRol());
        MEETING_DAO.updateMeeting(newMeeting, oldMeeting);
        Meeting meetingRetrieved = MEETING_DAO.getMeeting(1);
        MEETING_DAO.deleteMeeting(newMeeting);
        Assert.assertNotEquals(oldMeeting.getAgreements(), meetingRetrieved.getAgreements());
    }
    
    @Test
    public void testCorrectMeetingUpdateDifferentComments(){
        INITIALIZER.preparedRequestMeetingTestCase();
        Meeting oldMeeting = MEETING_DAO.getMeeting(2);
        Meeting newMeeting = new Meeting(0, "2021-05-02", "17:00:00", "Plan de Estudios de ISOF", "2021-04-30", "Realizada", "Teams", "Asunto de la reunión", "1. Se necesita un plan de trabajo", "1. Realizar plan de trabajo", "OFIJO3IJ3OIDJ3O");
        newMeeting.setMeetingAgenda(oldMeeting.getMeetingAgenda());
        newMeeting.getMeetingAgenda().setTopics(oldMeeting.getMeetingAgenda().getTopics());
        newMeeting.getMeetingAgenda().setPrerequisites(oldMeeting.getMeetingAgenda().getPrerequisites());
        newMeeting.setAgreements(oldMeeting.getAgreements());
        newMeeting.addComment(new Comment(0, "¿Cuando era la reunion con el director de la FEI?", "Dr. Angel Juan Sanchez Garcia", "20:00:01", "2021-04-30"));
        newMeeting.addComment(new Comment(0, "Se acordó el para el primero de junio", "Dra. Maria Karen Cortes Verdín", "21:32:00", "2021-04-30"));
        newMeeting.addComment(new Comment(0, "Faltaron notas y acuerdos", "Dr. Jorge Octavio Ocharán Hernández", "22:01:00", "2021-04-30"));
        newMeeting.setAssistantsRol(oldMeeting.getAssistantsRol());
        MEETING_DAO.updateMeeting(newMeeting, oldMeeting);
        Meeting meetingRetrieved = MEETING_DAO.getMeeting(2);
        MEETING_DAO.deleteMeeting(newMeeting);
        Assert.assertNotEquals(oldMeeting.getComments(), meetingRetrieved.getComments());
    }
    
    @Test
    public void testCorrectMeetingUpdateDifferentAssistantRol(){
        INITIALIZER.preparedRequestMeetingTestCase();
        Meeting oldMeeting = MEETING_DAO.getMeeting(3);
        Meeting newMeeting = new Meeting(0, "2021-05-02", "17:00:00", "Plan de Estudios de ISOF", "2021-04-30", "Realizada", "Teams", "Asunto de la reunión", "1. Se necesita un plan de trabajo", "1. Realizar plan de trabajo", "OFIJO3IJ3OIDJ3O");
        newMeeting.setMeetingAgenda(oldMeeting.getMeetingAgenda());
        newMeeting.getMeetingAgenda().setTopics(oldMeeting.getMeetingAgenda().getTopics());
        newMeeting.getMeetingAgenda().setPrerequisites(oldMeeting.getMeetingAgenda().getPrerequisites());
        newMeeting.setAgreements(oldMeeting.getAgreements());
        newMeeting.setComments(oldMeeting.getComments());
        newMeeting.getAssistantsRol().add(new AssistantRol(0, "Angel Juan Sánchez García", "Secretario", 1));
        newMeeting.getAssistantsRol().add(new AssistantRol(0, "Jorge Octavio Ocharán Hernández", "Lider de discusión", 2));
        newMeeting.getAssistantsRol().add(new AssistantRol(0, "Juan Carlos Pérez Arriaga", "Tomador de tiempo", 3));
        MEETING_DAO.updateMeeting(newMeeting, oldMeeting);
        Meeting meetingRetrieved = MEETING_DAO.getMeeting(3);
        MEETING_DAO.deleteMeeting(newMeeting);
        Assert.assertNotEquals(oldMeeting.getAssistantsRol(), meetingRetrieved.getAssistantsRol());
    }
    
    @Test
    public void testCorrectMeetingUpdateWithoutAgendaMeetingAndAgreementsAndComments(){
        INITIALIZER.preparedRequestMeetingTestCase();
        Meeting oldMeeting = MEETING_DAO.getMeeting(29);
        String meetingProject = "Eventos de Semestre Julio-Diciembre 2021";
        Meeting newMeeting = new Meeting(0, "2021-05-02", "17:00:00", meetingProject, "2021-04-30", "Pendiente", "Teams", "Asunto de la reunión","1. Se necesita un plan de trabajo", "1. Realizar plan de trabajo", "OFIJO3IJ3OIDJ3O");
        MEETING_DAO.updateMeeting(newMeeting, oldMeeting);
        
        Meeting meetingRetrieved = MEETING_DAO.getMeeting(1);
        MEETING_DAO.deleteMeeting(newMeeting);
        Assert.assertNotEquals(oldMeeting.getMeetingProject(), meetingRetrieved.getMeetingProject());
    }
    
    @Test
    public void testCorrectMeetingUpdateNotChanges(){
        INITIALIZER.preparedRequestMeetingTestCase();
        Meeting oldMeeting = MEETING_DAO.getMeeting(3);
        
        MEETING_DAO.updateMeeting(oldMeeting, oldMeeting);
        
        Meeting meetingRetrieved = MEETING_DAO.getMeeting(3);
        MEETING_DAO.deleteMeeting(oldMeeting);
        Assert.assertEquals(oldMeeting, meetingRetrieved);
        
    }
    
    @Test
    public void testIncorrectMeetingUpdateInvalidTopicInformation(){
        INITIALIZER.preparedRequestMeetingTestCase();
        Meeting oldMeeting = MEETING_DAO.getMeeting(2);
        Meeting newMeeting = new Meeting(0, "2021-05-02", "17:00:00", "Plan de Estudios de ISOF", "2021-04-30", "Realizada", "Teams", "Asunto de la reunión","1. Se necesita un plan de trabajo", "1. Realizar plan de trabajo", "OFIJO3IJ3OIDJ3O");
        newMeeting.setMeetingAgenda(oldMeeting.getMeetingAgenda());
        newMeeting.getMeetingAgenda().getTopics().add(new Topic(0, "82:00:00", "18:10:00", "01:00:00","01:10:00","Plan de estudios ISOF", "Dra. María Karen Cortés Verdín", "Concluido"));
        newMeeting.getMeetingAgenda().getTopics().add(new Topic(0, "18:10:00", "50:10:00", "01:00:00", "01:00:00", "Eventos del Semestres Enero-Julio 2021", "Dr. Angel Juan Sanchez Garcia", "Concluido"));
        newMeeting.getMeetingAgenda().setPrerequisites(oldMeeting.getMeetingAgenda().getPrerequisites());
        newMeeting.setAgreements(oldMeeting.getAgreements());
        newMeeting.setComments(oldMeeting.getComments());
        newMeeting.setAssistantsRol(oldMeeting.getAssistantsRol());
        MEETING_DAO.updateMeeting(newMeeting, oldMeeting);
        Meeting meetingRetrieved = MEETING_DAO.getMeeting(2);
        INITIALIZER.cleanMeetingTestCaseData();
        Assert.assertEquals(oldMeeting, meetingRetrieved);
    }
}
