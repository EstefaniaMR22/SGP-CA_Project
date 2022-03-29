package model.dao.memberdao;

import model.dao.MiembroDAO;
import model.domain.CivilStatus;
import model.domain.Member;
import model.domain.StudyGrade;
import org.junit.Assert;
import org.junit.Test;
import assets.utils.DateFormatter;

import java.sql.SQLException;
import java.time.LocalDate;

public class AddMemberTest {
    @Test
    public void addMemberTest() throws SQLException {
        Member member = new Member();
        member.setName("Roberto");
        member.setPaternalLastname("Palazuelos");
        member.setMaternalLastname("Reyes");
        member.setNationality("Mexicano");
        member.setEducationalProgram("DERECHO");
        member.setPersonalNumber("9432");
        member.setRfc("SDK430kADS");
        member.setTelephone("334092392");
        member.setBirthState("Mexico");
        member.setCurp("fkads0f234");
        member.setCivilStatus(CivilStatus.DIVORCED);
        member.setUvEmail("ropalazuelos@gmail.com");
        member.setAdmissionDate(DateFormatter.getDateFromDatepickerValue(LocalDate.now()));
        member.setBirthDate(DateFormatter.getDateFromDatepickerValue(LocalDate.now()));
        member.setAditionalEmail("rob@uv.mx");
        member.setWorkTelephone("23849238");
        member.setHomeTelephone("23099942");
        member.setAppointment("Punto de entrada");
        member.setStudyArea("PLC");
        member.setMaxStudyGrade(StudyGrade.SUPERIOR_EDUCATION);
        String password = "hola";
        int actualId = new MiembroDAO().addMember(member,password);
        int unexpected = -1;
        Assert.assertNotEquals(unexpected, actualId);
    }

    @Test
    public void addSecondMemberTest() throws SQLException {
        Member member = new Member();
        member.setName("Raul");
        member.setPaternalLastname("Sanchez9");
        member.setMaternalLastname("Reyes");
        member.setNationality("Mexicano");
        member.setEducationalProgram("INGENIERIA DE SOFTWARE");
        member.setPersonalNumber("9433");
        member.setRfc("SDK430kASDS");
        member.setTelephone("334092392");
        member.setBirthState("Mexico");
        member.setCurp("fkads0f234");
        member.setCivilStatus(CivilStatus.DIVORCED);
        member.setUvEmail("raulsanchezreyes@gmail.com");
        member.setAdmissionDate(DateFormatter.getDateFromDatepickerValue(LocalDate.now()));
        member.setBirthDate(DateFormatter.getDateFromDatepickerValue(LocalDate.now()));
        member.setAditionalEmail("rob@uv.mx");
        member.setWorkTelephone("23869529");
        member.setHomeTelephone("26059955");
        member.setAppointment("Ingenieria de software");
        member.setStudyArea("PLC");
        member.setMaxStudyGrade(StudyGrade.PRIMARY_EDUCATION);
        String password = "hola";
        int actualId = new MiembroDAO().addMember(member,password);
        int unexpected = -1;
        Assert.assertNotEquals(unexpected, actualId);
    }

}
