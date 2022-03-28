package model.dao.academicgroupdao;

import model.dao.AcademicGroupDAO;
import model.domain.AcademicGroup;
import model.domain.ConsolidationGrade;
import model.domain.LGAC;
import model.domain.Member;
import model.domain.Participation;
import model.domain.ParticipationType;
import org.junit.Assert;
import org.junit.Test;
import utils.DateFormatter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddAcademicGroupTest {
    @Test
    public void addAcademicGroupProgramTest() throws SQLException {
        AcademicGroup academicGroup = new AcademicGroup();
        academicGroup.setId("UV-CA-387");
        academicGroup.setName("Tecnologia Sustentable de las Obras de Ingenieria");
        academicGroup.setVision("Desarrollar unicamente las tecnicas para el desarrollo de las obras de ingenieria con un enfoque bayesiano");
        academicGroup.setGeneralObjetive("Ingenieria enfoque bayesiano");
        academicGroup.setMission("Desarollar tecnicas con fundamentos en las matematicas");
        academicGroup.setConsolidationGrade(ConsolidationGrade.IN_FORMATION);
        academicGroup.setRegisterDate(DateFormatter.getDateFromDatepickerValue(LocalDate.now()));
        academicGroup.setLastEvaluationDate(DateFormatter.getDateFromDatepickerValue(LocalDate.now()));
        academicGroup.setAdscriptionArea("Tecnica");
        academicGroup.setAdscriptionUnit("Facultad de Ingenieria");
        academicGroup.setDescriptionAdscription("Sin descripcion");
        String idAcademicGroup = new AcademicGroupDAO().addAcademicGroupProgram(academicGroup);
        Assert.assertNotNull(idAcademicGroup);
    }

    @Test
    public void addAcademicGroupWithMembersTest() throws SQLException {
        AcademicGroup academicGroup = new AcademicGroup();
        academicGroup.setId("UV-CA-388");
        academicGroup.setName("Fortalecimiento de cuerpo academico");
        academicGroup.setVision("Desarrollar metodos, tecnicas y herramientas para el desarrollo de software con un enfoque sistematico, disciplinado y cuantificale y apegado a estandares de calidad");
        academicGroup.setGeneralObjetive("Generar conocimento y formas recursos humanos en ingenieria de software que contribuyan al desarrollo de software de calidad.");
        academicGroup.setMission("Difusion de informacion en foros especializados y de divulgacion, fortalecimiento de la vinculacion academia industria");
        academicGroup.setConsolidationGrade(ConsolidationGrade.IN_FORMATION);
        academicGroup.setRegisterDate((DateFormatter.getDateFromDatepickerValue(LocalDate.now())));
        academicGroup.setLastEvaluationDate(DateFormatter.getDateFromDatepickerValue(LocalDate.now()));
        academicGroup.setAdscriptionArea("Economico Administrativa");
        academicGroup.setAdscriptionUnit("Facultad de Estadistica e Informatica");
        academicGroup.setDescriptionAdscription("Sin descripcion.");
        List<Participation> participationList = new ArrayList<>();
        Member member = new Member();
        member.setId(2);
        Participation participation = new Participation();
        participation.setParticipationType(ParticipationType.INTEGRANT);
        participation.setMember(member);
        Member member1 = new Member();
        member1.setId(3);
        participationList.add(participation);
        Participation participation1 = new Participation();
        participation1.setParticipationType(ParticipationType.INTEGRANT);
        participation1.setMember(member1);
        participationList.add(participation1);
        academicGroup.setParticipationList(participationList);
        String idAcademicGroup = new AcademicGroupDAO().addAcademicGroupProgram(academicGroup);
        Assert.assertNotNull(idAcademicGroup);
    }

    @Test
    public void addAcademicGroupProgramWithLgacsTest() throws SQLException {
        AcademicGroup academicGroup = new AcademicGroup();
        academicGroup.setId("UV-CA-389");
        academicGroup.setName("Tecnologia Sustentable de las Obras de Ingenieria");
        academicGroup.setVision("Desarrollar unicamente las tecnicas para el desarrollo de las obras de ingenieria con un enfoque bayesiano");
        academicGroup.setGeneralObjetive("Ingenieria enfoque bayesiano");
        academicGroup.setMission("Desarollar tecnicas con fundamentos en las matematicas");
        academicGroup.setConsolidationGrade(ConsolidationGrade.IN_FORMATION);
        academicGroup.setRegisterDate(DateFormatter.getDateFromDatepickerValue(LocalDate.now()));
        academicGroup.setLastEvaluationDate(DateFormatter.getDateFromDatepickerValue(LocalDate.now()));
        academicGroup.setAdscriptionArea("Tecnica");
        academicGroup.setAdscriptionUnit("Facultad de Ingenieria");
        academicGroup.setDescriptionAdscription("Sin descripcion");
        List<LGAC> lgacList = new ArrayList<>();
        LGAC lgac = new LGAC();
        lgac.setIdentification("test");
        lgac.setDescription("La seguridad de las estructuras en la ingenieria civil, desarrollo e investigacion tecnologica");
        lgacList.add(lgac);
        LGAC lgac1 = new LGAC();
        lgac.setIdentification("test2");
        lgac.setDescription("Tecnologia sustentable de las obras de ingenieria");
        lgacList.add(lgac1);
        academicGroup.setLgacList(lgacList);
        String idAcademicGroup = new AcademicGroupDAO().addAcademicGroupProgram(academicGroup);
        Assert.assertNotNull(idAcademicGroup);
    }

    @Test
    public void addAcademicGroupProgramWithLgacsAndMembersTest() throws SQLException {
        AcademicGroup academicGroup = new AcademicGroup();
        academicGroup.setId("UV-CA-390");
        academicGroup.setName("Obras de Artes Visuales");
        academicGroup.setVision("Desarrollo de nuevas abstracciones en el arte barroco");
        academicGroup.setGeneralObjetive("Aplicar conocimientos en el arte barroco");
        academicGroup.setMission("Arte barroco");
        academicGroup.setConsolidationGrade(ConsolidationGrade.IN_FORMATION);
        academicGroup.setRegisterDate(DateFormatter.getDateFromDatepickerValue(LocalDate.now()));
        academicGroup.setLastEvaluationDate(DateFormatter.getDateFromDatepickerValue(LocalDate.now()));
        academicGroup.setAdscriptionArea("Artes");
        academicGroup.setAdscriptionUnit("Casa Lagos UV");
        academicGroup.setDescriptionAdscription("Sin descripcion");
        List<LGAC> lgacList = new ArrayList<>();
        LGAC lgac = new LGAC();
        lgac.setIdentification("test");
        lgac.setDescription("Arte barroco en su maxima expresion");
        lgacList.add(lgac);
        LGAC lgac1 = new LGAC();
        lgac1.setIdentification("test2");
        lgac1.setDescription("Arte griego sumado al arte barroco");
        lgacList.add(lgac1);
        academicGroup.setLgacList(lgacList);
        List<Participation> participationList = new ArrayList<>();
        Member member = new Member();
        member.setId(2);
        Participation participation = new Participation();
        participation.setParticipationType(ParticipationType.INTEGRANT);
        participation.setMember(member);
        Member member1 = new Member();
        member1.setId(3);
        participationList.add(participation);
        Participation participation1 = new Participation();
        participation1.setParticipationType(ParticipationType.INTEGRANT);
        participation1.setMember(member1);
        participationList.add(participation1);
        academicGroup.setParticipationList(participationList);
        String idAcademicGroup = new AcademicGroupDAO().addAcademicGroupProgram(academicGroup);
        Assert.assertNotNull(idAcademicGroup);
    }
}
