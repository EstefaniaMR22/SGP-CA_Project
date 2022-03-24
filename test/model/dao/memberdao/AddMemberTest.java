package model.dao.memberdao;

import model.dao.MiembroDAO;
import model.domain.CivilStatus;
import model.domain.ParticipationType;
import model.domain.StudyGrade;
import org.junit.Assert;
import org.junit.Test;
import utils.DateFormatter;

import java.sql.SQLException;
import java.time.LocalDate;

public class AddMemberTest {
    @Test
    public void addMemberIntegrantTest() throws SQLException {
        Integrant integrant = new Integrant();
        integrant.setName("Roberto");
        integrant.setPaternalLastname("Palazuelos");
        integrant.setMaternalLastname("Reyes");
        integrant.setNationality("Mexicano");
        integrant.setEducationalProgram("Derecho");
        integrant.setPersonalNumber("a92");
        integrant.setRfc("SDK430kADS");
        integrant.setTelephone("334092392");
        integrant.setBirthState("Mexico");
        integrant.setCurp("fkads0f234");
        integrant.setCivilStatus(CivilStatus.DIVORCED);
        integrant.setUvEmail("ropalazuelos@gmail.com");
        integrant.setParticipationType(ParticipationType.RESPONSABLE);
        integrant.setAdmissionDate(DateFormatter.getDateFromDatepickerValue(LocalDate.now()));
        integrant.setBirthDate(DateFormatter.getDateFromDatepickerValue(LocalDate.now()));
        integrant.setAditionalEmail("rob@uv.mx");
        integrant.setWorkTelephone("23849238");
        integrant.setHomeTelephone("23099942");
        integrant.setAppointment("Punto de entrada");
        String password = "hola";
        int actualId = new MiembroDAO().addMember(integrant,password);
        int unexpected = -1;
        Assert.assertNotEquals(unexpected, actualId);
    }

    @Test
    public void addMemberColaboratorTest() throws SQLException {
        Colaborator colaborator = new Colaborator();
        colaborator.setName("Gilberto");
        colaborator.setPaternalLastname("Reyes");
        colaborator.setMaternalLastname("Reyes");
        colaborator.setNationality("Mexicano");
        colaborator.setEducationalProgram("Ingenieria de Software");
        colaborator.setPersonalNumber("4fsid02");
        colaborator.setRfc("SDK43sadf");
        colaborator.setTelephone("234892324");
        colaborator.setBirthState("Mexico");
        colaborator.setCurp("fkadsads0f234");
        colaborator.setCivilStatus(CivilStatus.DIVORCED);
        colaborator.setUvEmail("ropfasdlos@gmail.com");
        colaborator.setParticipationType(ParticipationType.RESPONSABLE);
        colaborator.setAdmissionDate(DateFormatter.getDateFromDatepickerValue(LocalDate.now()));
        colaborator.setBirthDate(DateFormatter.getDateFromDatepickerValue(LocalDate.now()));
        colaborator.setStudyArea("Ultima area");
        colaborator.setMaxStudyGrade(StudyGrade.SUPERIOR_EDUCATION);
        int actualId = new MiembroDAO().addMember(colaborator);
        int unexpected = -1;
        Assert.assertNotEquals(unexpected, actualId);
    }

    @Test
    public void addMemberResponsableTest() throws SQLException {
        Responsable responsable = new Responsable();
        responsable.setName("Javier");
        responsable.setPaternalLastname("Bravo");
        responsable.setMaternalLastname("Garcia");
        responsable.setNationality("Mexicano");
        responsable.setEducationalProgram("Tecnologias computacionales");
        responsable.setPersonalNumber("afsdk");
        responsable.setRfc("SDK430kDS");
        responsable.setTelephone("32943230");
        responsable.setBirthState("Mexico");
        responsable.setCurp("apsodkfa09sa32");
        responsable.setCivilStatus(CivilStatus.DIVORCED);
        responsable.setUvEmail("afdskoos@gmail.com");
        responsable.setParticipationType(ParticipationType.RESPONSABLE);
        responsable.setAdmissionDate(DateFormatter.getDateFromDatepickerValue(LocalDate.now()));
        responsable.setBirthDate(DateFormatter.getDateFromDatepickerValue(LocalDate.now()));
        responsable.setAditionalEmail("fadb@uv.mx");
        responsable.setWorkTelephone("23849238");
        responsable.setHomeTelephone("23099942");
        responsable.setAppointment("Punto de entrada");
        String password = "hola";
        int actualId = new MiembroDAO().addMember(responsable,password);
        int unexpected = -1;
        Assert.assertNotEquals(unexpected, actualId);
    }





}
