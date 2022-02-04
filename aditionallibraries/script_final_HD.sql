CREATE TABLE `GeneralResume` (
	`bodyAcademyKey` varchar(15) NOT NULL,
	`nameBA` varchar(100) DEFAULT NULL,
    `areaAscription` varchar (500) DEFAULT NULL,
    `ascriptionUnit` varchar (500) DEFAULT NULL,
    `consolidationDegree` varchar (50) DEFAULT NULL,
    `registrationDate` date DEFAULT NULL,
    `lastEvaluationDate` date DEFAULT NULL,
    `vision` varchar (5000) DEFAULT NULL,
    `mission` varchar (5000) DEFAULT NULL,
    `generalTarjet` varchar(5000) DEFAULT NULL,
    PRIMARY KEY (`bodyAcademyKey`)
);

CREATE TABLE `Lgak` (
	`identifierLgak` int(2) auto_increment,
    `bodyAcademyKey` varchar(10) DEFAULT NULL,
    `title` varchar(500) DEFAULT NULL,
    `description` varchar(500) DEFAULT NULL,
    PRIMARY KEY (`IdentifierLgak`),
    CONSTRAINT `fk_lgac_1` FOREIGN KEY (`bodyAcademyKey`) REFERENCES `GeneralResume` (`bodyAcademyKey`)
    ON UPDATE CASCADE
);

CREATE TABLE `Collaborator` (
	`rfc` varchar(20) NOT NULL,
    `bodyAcademyKey` varchar(15) DEFAULT NULL,
	`fullName` varchar(500) NOT NULL,
	`dateOfAdmission` date DEFAULT NULL,
    `emailUV` varchar(500) DEFAULT NULL,
    `participationStatus` varchar(50) NOT NULL,
    `curp` varchar(20) DEFAULT NULL,
    `nacionality` varchar(100) DEFAULT NULL,
    `educationalProgram` varchar(100) NOT NULL,
    `cellPhone` varchar(10) DEFAULT NULL,
    `satffNumber` int(6) DEFAULT NULL,
    `studyArea` varchar(100) DEFAULT NULL,
    `nameBACollaborator` varchar(500),
    `highestDegreeStudies` varchar(100) DEFAULT NULL,
    `participationType` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`rfc`),
    KEY `fk_member_1` (`bodyAcademyKey`),
    CONSTRAINT `fk_member_1` FOREIGN KEY (`bodyAcademyKey`) REFERENCES `GeneralResume` (`bodyAcademyKey`)
    ON UPDATE CASCADE
);

CREATE TABLE `Integrant` (
	`rfc` varchar(20) NOT NULL,
    `bodyAcademyKey` varchar(15) DEFAULT NULL,
	`fullName` varchar(500) DEFAULT NULL,
	`dateOfAdmission` date DEFAULT NULL,
    `emailUV` varchar(500) DEFAULT NULL,
    `password` varchar(50) NOT NULL,
    `participationStatus` varchar(50) NOT NULL,
    `additionalEmail` varchar(500) DEFAULT NULL,
    `curp` varchar(20) DEFAULT NULL,
    `nacionality` varchar(100) DEFAULT NULL,
    `educationalProgram` varchar(100) NOT NULL,
    `cellPhone` varchar(10) DEFAULT NULL,
	`satffNumber` int(6) DEFAULT NULL,
    `homePhone` varchar(10) DEFAULT NULL,
    `workPhone` varchar(20) DEFAULT NULL,
	`appointment` varchar(50) DEFAULT NULL,
    `participationType` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`rfc`),
    CONSTRAINT `fk_integrant_1` FOREIGN KEY (`bodyAcademyKey`) REFERENCES `GeneralResume` (`bodyAcademyKey`)
    ON UPDATE CASCADE
);

CREATE TABLE `Schooling` (
	`professionalId` varchar(10) NOT NULL,
    `rfc` varchar(20) NOT NULL,
    `studiesInsitution` varchar(500) DEFAULT NULL,
    `levelOfStudy` varchar(50) DEFAULT NULL,
    `dateOfObtainingStudies` date DEFAULT NULL,
    `studiesState` varchar(100) DEFAULT NULL,
    `studyDiscipline` varchar(100) DEFAULT NULL,
    `studyArea` varchar(100) DEFAULT NULL,
    `studyName` varchar(100) DEFAULT NULL,
	PRIMARY KEY (`professionalID`),
    FOREIGN KEY (`rfc`) REFERENCES `Integrant` (`rfc`)
    ON UPDATE CASCADE
);

CREATE TABLE `Project` (
	`projectName` varchar(500) NOT NULL,
    `bodyAcademyKey` varchar(10) DEFAULT NULL,
    `durationProjectInMonths` int(2) DEFAULT NULL,
    `status` varchar(10) DEFAULT NULL,
    `startDate` date DEFAULT NULL,
    `endDate` date DEFAULT NULL,
    `estimatedEndDate` date DEFAULT NULL,
	`description` varchar(500) DEFAULT NULL,
    PRIMARY KEY (`projectName`),
	KEY `fk_project_1` (`bodyAcademyKey`),
    CONSTRAINT `fk_project_1` FOREIGN KEY (`bodyAcademyKey`) REFERENCES `GeneralResume` (`bodyAcademyKey`)
    ON UPDATE CASCADE
);

CREATE TABLE `LgakProject` (
	`identifierLgak` int(2) NOT NULL auto_increment,
	`title` varchar(500) NOT NULL,
	`projectName` varchar(500) NOT NULL,
	PRIMARY KEY (`identifierLgak`),
	CONSTRAINT `fk_lgakProject_1` FOREIGN KEY (`identifierLgak`) REFERENCES `Lgak` (`identifierLgak`)
	ON UPDATE CASCADE,
	CONSTRAINT `fk_lgakProject_3` FOREIGN KEY (`projectName`) REFERENCES `Project` (`projectName`)
	ON UPDATE CASCADE
);

CREATE TABLE `Book` (
	`urlFile`  varchar(100) NOT NULL,
	`projectName` varchar(80) DEFAULT NULL,
    `impactBA` boolean DEFAULT NULL,
    `evidenceType` varchar(50) DEFAULT NULL,
	`evidenceTitle` varchar(80) DEFAULT NULL,
    `registrationResponsible` varchar(100) NOT NULL,
    `registrationDate` date NOT NULL,
    `studyDegree` varchar(50) DEFAULT NULL,
    `publicationDate` date DEFAULT NULL,
    `country` varchar(90) DEFAULT NULL,
    `publisher` varchar(30) DEFAULT NULL,
    `editionsNumber` int(2) DEFAULT NULL,
    `isbn` int(13) DEFAULT NULL,
    PRIMARY KEY (`urlFile`),
	KEY `fk_book_1` (`projectName`),
    CONSTRAINT `fk_book_1` FOREIGN KEY (`projectName`) REFERENCES `Project` (`projectName`)
    ON UPDATE CASCADE
);

CREATE TABLE `BookStudent` (
	`urlFile`  varchar(100) NOT NULL,
    `student`  varchar(50) NOT NULL,
    PRIMARY KEY (`urlFile`, `student`),
    KEY `fk_bookStudent_1` (`urlFile`),
    CONSTRAINT `fk_bookStudent_1` FOREIGN KEY (`urlFile`) REFERENCES `Book` (`urlFile`)
    ON UPDATE CASCADE
);

CREATE TABLE `IntegrantBook` (
	`rfc` varchar(20) NOT NULL,
    `urlFile`  varchar(100) NOT NULL,
    PRIMARY KEY (`urlFile`, `rfc`),
    KEY `fk_integrantBook_1` (`rfc`),
    CONSTRAINT `fk_integrantBook_1` FOREIGN KEY (`rfc`) REFERENCES `Integrant` (`rfc`) ON UPDATE cascade,
    KEY `fk_integrantBook_2` (`urlFile`),
    CONSTRAINT `fk_integrantBook_2` FOREIGN KEY (`urlFile`) REFERENCES `Book` (`urlFile`)
    ON UPDATE CASCADE
);

CREATE TABLE `CollaborateBook` (
	`rfc` varchar(20) NOT NULL,
    `urlFile`  varchar(100) NOT NULL,
    PRIMARY KEY (`urlFile`, `rfc`),
    KEY `fk_collaborateBook_1` (`rfc`),
    CONSTRAINT `fk_collaborateBook_1` FOREIGN KEY (`rfc`) REFERENCES `Collaborator` (`rfc`) ON UPDATE CASCADE,
    KEY `fk_collaborateBook_2` (`urlFile`),
    CONSTRAINT `fk_collaborateBook_2` FOREIGN KEY (`urlFile`) REFERENCES `Book` (`urlFile`)
    ON UPDATE CASCADE
);

CREATE TABLE `ChapterBook` (
	`urlFile` varchar(100) NOT NULL,
    `chapterBookTitle` varchar(80) DEFAULT NULL,
    `registrationDate` date DEFAULT NULL,
    `registrationResponsible` varchar(80) DEFAULT NULL,
    `pages-number` varchar(10) DEFAULT NULL,
    `urlFileBook`  varchar(100) DEFAULT NULL,
	PRIMARY KEY (`urlFile`),
    KEY `fk_chapterBook_1` (`urlFileBook`),
    CONSTRAINT `fk_chapterBook_1` FOREIGN KEY (`urlFileBook`) REFERENCES `Book` (`urlFile`)
    ON UPDATE CASCADE
);

CREATE TABLE `ChapterbookStudent` (
	`urlFile`  varchar(100) NOT NULL,
    `student`  varchar(50) DEFAULT NULL,
    PRIMARY KEY (`urlFile`, `student`),
    KEY `fk_chapterBookStudent_1` (`urlFile`),
    CONSTRAINT `fk_chapterBookStudent_1` FOREIGN KEY (`urlFile`) REFERENCES `ChapterBook` (`urlFile`)
    ON UPDATE CASCADE
);

CREATE TABLE `IntegrantChapterbook` (
	`rfc` varchar(20) NOT NULL,
    `urlFile`  varchar(100) NOT NULL,
    PRIMARY KEY (`urlFile`, `rfc`),
    KEY `fk_integrantChapterBook_1` (`rfc`),
    CONSTRAINT `fk_integrantChapterBook_1` FOREIGN KEY (`rfc`) REFERENCES `Integrant` (`rfc`) ON UPDATE CASCADE,
    KEY `fk_integrantChapterBook_2` (`urlFile`),
    CONSTRAINT `fk_integrantChapterBook_2` FOREIGN KEY (`urlFile`) REFERENCES `ChapterBook` (`urlFile`)
    ON UPDATE CASCADE
);

CREATE TABLE `CollaborateChapterbook` (
	`rfc` varchar(20) NOT NULL,
    `urlFile`  varchar(100) NOT NULL,
    PRIMARY KEY (`urlFile`, `rfc`),
    KEY `fk_collaborateChapterBook_1` (`rfc`),
    CONSTRAINT `fk_collaborateChapterBook_1` FOREIGN KEY (`rfc`) REFERENCES `Collaborator` (`rfc`) ON UPDATE CASCADE,
    KEY `fk_collaborateChapterBook_2` (`urlFile`),
    CONSTRAINT `fk_collaborateChapterBook_2` FOREIGN KEY (`urlFile`) REFERENCES `ChapterBook` (`urlFile`)
    ON UPDATE CASCADE
);

CREATE TABLE `Prototype` (
	`urlFile`  varchar(100) NOT NULL,
	`projectName` varchar(80) DEFAULT NULL,
    `impactBA` boolean DEFAULT NULL,
    `evidenceType` varchar(50) DEFAULT NULL,
	`evidenceTitle` varchar(80) DEFAULT NULL,
    `registrationResponsible` varchar(100) NOT NULL,
    `registrationDate` date NOT NULL,
    `studyDegree` varchar(50) DEFAULT NULL,
    `publicationDate` date DEFAULT NULL,
    `country` varchar(90) DEFAULT NULL,
	`feautures` varchar(150) DEFAULT NULL,
    PRIMARY KEY (`urlFile`),
    CONSTRAINT `fk_prototype_1` FOREIGN KEY (`projectName`) REFERENCES `Project` (`projectName`)
	ON UPDATE CASCADE
);

CREATE TABLE `PrototypeStudent` (
	`urlFile`  varchar(100) NOT NULL,
    `student`  varchar(50) DEFAULT NULL,
    PRIMARY KEY (`urlFile`, `student`),
    CONSTRAINT `fk_prototypeStudent_1` FOREIGN KEY (`urlFile`) REFERENCES `Prototype` (`urlFile`)
    ON UPDATE CASCADE
);

/fejfeoifjeofjeo
CREATE TABLE `IntegrantPrototype` (
	`rfc` varchar(20) NOT NULL,
    `urlFile`  varchar(100) NOT NULL,
    PRIMARY KEY (`urlFile`, `rfc`),
	CONSTRAINT `fk_prototypeInte_1` FOREIGN KEY (`urlFile`) REFERENCES `Prototype` (`urlFile`)
    ON UPDATE CASCADE,
    CONSTRAINT `fk_prototypeInte_2` FOREIGN KEY (`rfc`) REFERENCES `Integrant` (`rfc`)
    ON UPDATE CASCADE
);


CREATE TABLE `CollaboratePrototype` (
	`rfc` varchar(20) NOT NULL,
    `urlFile`  varchar(100) NOT NULL,
    PRIMARY KEY (`urlFile`, `rfc`),
    CONSTRAINT `fk_prototypeColl_1` FOREIGN KEY (`urlFile`) REFERENCES `Prototype` (`urlFile`)
    ON UPDATE CASCADE,
    CONSTRAINT `fk_prototypeColl_2` FOREIGN KEY (`rfc`) REFERENCES `Collaborator` (`rfc`)
    ON UPDATE CASCADE
);

CREATE TABLE `Article` (
	`urlFile`  varchar(100) NOT NULL,
	`projectName` varchar(80) DEFAULT NULL,
    `impactBA` boolean DEFAULT NULL,
    `evidenceType` varchar(50) DEFAULT NULL,
	`evidenceTitle` varchar(80) DEFAULT NULL,
    `registrationResponsible` varchar(100) NOT NULL,
    `registrationDate` date NOT NULL,
    `studyDegree` varchar(50) DEFAULT NULL,
    `publicationDate` date DEFAULT NULL,
    `country` varchar(90) DEFAULT NULL,
    `isnn` int(13) DEFAULT NULL,
    `editorialName` varchar(200) DEFAULT NULL,
    `magazineName` varchar(200) DEFAULT NULL,
    PRIMARY KEY (`urlFile`),
    CONSTRAINT `fk_article_52` FOREIGN KEY (`projectName`) REFERENCES `Project` (`projectName`)
    ON UPDATE CASCADE
);

CREATE TABLE `ArticleStudent` (
	`urlFile`  varchar(100) NOT NULL,
    `student`  varchar(50) NOT NULL,
    PRIMARY KEY (`urlFile`, `student`),
    CONSTRAINT `fk_article_4` FOREIGN KEY (`urlFile`) REFERENCES `Article` (`urlFile`)
    ON UPDATE CASCADE
);

CREATE TABLE `IntegrantArticle` (
	`rfc` varchar(20) NOT NULL,
    `urlFile`  varchar(100) NOT NULL,
    PRIMARY KEY (`urlFile`, `rfc`),
    CONSTRAINT `fk_article_6` FOREIGN KEY (`urlFile`) REFERENCES `Article` (`urlFile`)
    ON UPDATE CASCADE,
    CONSTRAINT `fk_article_7` FOREIGN KEY (`rfc`) REFERENCES `Integrant` (`rfc`)
    ON UPDATE CASCADE
);


CREATE TABLE `CollaborateArticle` (
	`rfc` varchar(20) NOT NULL,
    `urlFile`  varchar(100) NOT NULL,
    PRIMARY KEY (`urlFile`, `rfc`),
    CONSTRAINT `fk_article_5` FOREIGN KEY (`urlFile`) REFERENCES `Article` (`urlFile`)
    ON UPDATE CASCADE,
    CONSTRAINT `fk_article_8` FOREIGN KEY (`rfc`) REFERENCES `Collaborator` (`rfc`)
    ON UPDATE CASCADE
);

CREATE TABLE `ReceptionWork` (
	`urlFile`  varchar(100) NOT NULL,
	`projectName` varchar(80) DEFAULT NULL,
    `impactBA` boolean DEFAULT NULL,
    `evidenceType` varchar(50) DEFAULT NULL,
	`evidenceTitle` varchar(80) DEFAULT NULL,
    `registrationResponsible` varchar(100) NOT NULL,
    `registrationDate` date NOT NULL,
    `studyDegree` varchar(50) DEFAULT NULL,
    `publicationDate` date DEFAULT NULL,
    `country` varchar(90) DEFAULT NULL,
    `description` varchar(500) DEFAULT NULL,
    `status` varchar(10) DEFAULT NULL,
    `actualDurationInMonths` int(2) DEFAULT NULL,
    `estimatedDurationInMonths` int(2) DEFAULT NULL,
    `modality` varchar(200) DEFAULT NULL,
	PRIMARY KEY (`urlFile`),
    KEY `fk_receptionwork_1` (`projectName`),
    CONSTRAINT `fk_receptionwork_1` FOREIGN KEY (`projectName`) REFERENCES `Project` (`projectName`)
    ON UPDATE CASCADE
);

CREATE TABLE `ReceptionWorkRequirement` (
	`urlFile`  varchar(100) NOT NULL,
    `requirement` varchar(80) DEFAULT NULL,
    KEY `fk_receptionworkrequirement_1` (`urlFile`),
    CONSTRAINT `fk_receptionworkrequirement_1` FOREIGN KEY (`urlFile`) REFERENCES `ReceptionWork` (`urlFile`)
    ON UPDATE CASCADE
);

CREATE TABLE `ReceptionWorkStudent` (
	`urlFile`  varchar(100) NOT NULL,
    `student`  varchar(50) NOT NULL,
    PRIMARY KEY (`urlFile`, `student`),
    KEY `fk_receptioWorkStudent_1` (`urlFile`),
    CONSTRAINT `fk_receptioWorkStudent_1` FOREIGN KEY (`urlFile`) REFERENCES `ReceptionWork` (`urlFile`)
    ON UPDATE CASCADE
);

CREATE TABLE `IntegrantReceptionWork` (
	`rfc` varchar(20) NOT NULL,
    `urlFile`  varchar(100) NOT NULL,
    PRIMARY KEY (`urlFile`, `rfc`),
    KEY `fk_integrantReceptionWork_1` (`rfc`),
    CONSTRAINT `fk_integrantReceptionWork_1` FOREIGN KEY (`rfc`) REFERENCES `Integrant` (`rfc`) ON UPDATE CASCADE,
    KEY `fk_integrantReceptionWork_2` (`urlFile`),
    CONSTRAINT `fk_integrantReceptionWork_2` FOREIGN KEY (`urlFile`) REFERENCES `ReceptionWork` (`urlFile`)
    ON UPDATE CASCADE
);

CREATE TABLE `CollaborateReceptionWork` (
	`rfc` varchar(20) NOT NULL,
    `urlFile`  varchar(100) NOT NULL,
    PRIMARY KEY (`urlFile`, `rfc`),
    KEY `fk_collaborateReceptionWork_1` (`rfc`),
    CONSTRAINT `fk_collaborateReceptionWork_1` FOREIGN KEY (`rfc`) REFERENCES `Collaborator` (`rfc`) ON UPDATE CASCADE,
    KEY `fk_collaborateReceptionWork_2` (`urlFile`),
    CONSTRAINT `fk_collaborateReceptionWork_2` FOREIGN KEY (`urlFile`) REFERENCES `ReceptionWork` (`urlFile`)
    ON UPDATE CASCADE
);

CREATE TABLE `Meeting` (
	`meetingKey` int auto_increment,
	`meetingDate` date NOT NULL,
	`meetingTime` time NOT NULL,
    `meetingProject` varchar(80) DEFAULT NULL,
    `meetingRegistrationDate` date DEFAULT NULL,
    `statusMeeting` varchar(15) DEFAULT NULL,
    `placeMeeting` varchar(50) DEFAULT NULL,
    `issueMeeting` varchar(200) DEFAULT NULL,
    `meetingNote` varchar(3000) DEFAULT NULL,
    `meetingPending` varchar(3000) DEFAULT NULL,
    `integrantResponsible` varchar(20) NOT NULL,
    PRIMARY KEY (`meetingKey`),
    CONSTRAINT `fk_meeting_1` FOREIGN KEY (`integrantResponsible`) REFERENCES `Integrant` (`rfc`)
    ON UPDATE CASCADE
);

CREATE TABLE `MeetingAgenda` (
	`meetingAgendaKey` int auto_increment,
    `meetingKey` int DEFAULT NULL,
    `totalTime` time DEFAULT NULL,
    `estimatedTotalTime` time DEFAULT NULL,
    `numberTopics` int(2) DEFAULT NULL,
    PRIMARY KEY (`meetingAgendaKey`),
    KEY `fk_meetingAgenda_1` (`meetingKey`),
    CONSTRAINT `fk_meetingAgenda_1` FOREIGN KEY (`meetingKey`) REFERENCES `Meeting` (`meetingKey`)
    ON UPDATE CASCADE
);

CREATE TABLE `IntegrantMeeting` (
	`integrantMeetingKey` int auto_increment,
	`assistantName` varchar(60) DEFAULT NULL,
    `meetingKey` int DEFAULT NULL,
    `role` varchar(15) DEFAULT NULL,
    `assistantNumber` int(2) DEFAULT NULL,
    `initials` varchar(8) DEFAULT NULL,
    PRIMARY KEY (`integrantMeetingKey`),
    KEY `fk_integrantmeeting_2` (`meetingKey`),
    CONSTRAINT `fk_integrantmeeting_2` FOREIGN KEY (`meetingKey`) REFERENCES `Meeting` (`meetingKey`)
    ON UPDATE CASCADE
);

CREATE TABLE `Prerequisite` (
	`prerequisiteKey` int auto_increment,
    `meetingAgendaKey` int DEFAULT NULL,
    `responsiblePrerequisite` varchar(50) DEFAULT NULL,
    `descriptionPrerequisite` varchar(200) DEFAULT NULL,
    PRIMARY KEY (`prerequisiteKey`),
    KEY `fk_prerequisite_1` (`meetingAgendaKey`),
    CONSTRAINT `fk_prerequisite_1` FOREIGN KEY (`meetingAgendaKey`) REFERENCES `MeetingAgenda` (`meetingAgendaKey`)
    ON UPDATE CASCADE
);

CREATE TABLE `Topic` (
	`numberTopic` int auto_increment,
    `meetingAgendaKey` int DEFAULT NULL,
    `startTime` time DEFAULT NULL,
    `endTime` time DEFAULT NULL,
    `plannedTime` time DEFAULT NULL,
    `realTime` time DEFAULT NUll,
    `descriptionTopic` varchar(200) DEFAULT NULL,
    `discissionLeader` varchar(50) DEFAULT NULL,
    `statusTopic` varchar(20) DEFAULT NULL,
    PRIMARY KEY (`numberTopic`),
    KEY `fk_topic_1` (`meetingAgendaKey`),
    CONSTRAINT `fk_topic_1` FOREIGN KEY (`meetingAgendaKey`) REFERENCES `MeetingAgenda` (`meetingAgendaKey`)
    ON UPDATE CASCADE
);

CREATE TABLE `Agreement` (
	`agreementNumber` int auto_increment,
    `meetingKey` int DEFAULT NULL,
    `descriptionAgreement` varchar(200) DEFAULT NULL,
    `responsibleAgreement` varchar(80) DEFAULT NULL,
    `deliveryDate` date DEFAULT NULL,
    PRIMARY KEY (`agreementNumber`),
    KEY `fk_agreement_1` (`meetingKey`),
    CONSTRAINT `fk_agreement_1` FOREIGN KEY (`meetingKey`) REFERENCES `Meeting` (`meetingKey`)
    ON UPDATE CASCADE
);


CREATE TABLE `Comment` (
	`commentKey` int auto_increment,  
    `meetingKey` int DEFAULT NULL,
    `commentDescription` varchar(300) DEFAULT NULL,
    `commentator` varchar(80) DEFAULT NULL,
    `commentTime` time DEFAULT NULL,
    `commentDate` date DEFAULT NULL,
    PRIMARY KEY (`commentKey`),
    KEY `fk_comment_1` (`meetingKey`),
    CONSTRAINT `fk_comment_1` FOREIGN KEY (`meetingKey`) REFERENCES `Meeting` (`meetingKey`)
    ON UPDATE CASCADE
);

CREATE TABLE `WorkPlan` (
	`workplanKey` int auto_increment,
    `bodyAcademyKey` varchar(10) DEFAULT NULL,
    `startDate` date DEFAULT NULL,
    `endDate` date DEFAULT NULL,
    `generalTarjet` varchar(100) DEFAULT NULL,
    `durationInYears` int DEFAULT NULL,
    PRIMARY KEY (`workplanKey`),
    CONSTRAINT `AK_Password` UNIQUE (`endDate`, `bodyAcademyKey`),
    CONSTRAINT `fk_wrokplan_1` FOREIGN KEY (`bodyAcademyKey`) REFERENCES `GeneralResume` (`bodyAcademyKey`)
    ON UPDATE CASCADE
);

CREATE TABLE `Goal` (
	`goalIdentifier` int auto_increment,
    `workplanKey` int(3) DEFAULT NULL,
    `startDate` date DEFAULT NULL,
    `endDate` date DEFAULT NULL,
    `statusGoal` boolean DEFAULT NULL,
    `descriptionGoal` varchar(500) DEFAULT NULL,
    PRIMARY KEY (`goalIdentifier`),
    KEY `fk_goal_1` (`workplanKey`),
    CONSTRAINT `fk_goal_1` FOREIGN KEY (`workplanKey`) REFERENCES `WorkPlan` (`workplanKey`)
    ON UPDATE CASCADE
);

CREATE TABLE `Action` (
	`actionKey` int auto_increment,
    `goalIdentifier` int(5) DEFAULT NULL,
    `startDateAction` date DEFAULT NULL,
    `endDateAction` date DEFAULT NULL,
    `estimatedEndDate` date DEFAULT NULL,
    `statusAction` boolean DEFAULT NULL,
    `descriptionAction` varchar(5000) DEFAULT NULL,
    `responsibleAction` varchar(500) DEFAULT NULL,
    `resourse` varchar(500) DEFAULT NULL,
    PRIMARY KEY (`actionKey`),
    KEY `fk_action_1` (`goalIdentifier`),
    CONSTRAINT `fk_action_1` FOREIGN KEY (`goalIdentifier`) REFERENCES `Goal` (`goalIdentifier`)
    ON UPDATE CASCADE
);

CREATE VIEW `Evidences` AS 
SELECT Prototype.urlFile, evidenceType, evidenceTitle, impactBA, registrationResponsible, registrationDate, projectName, Integrant.emailUV, Integrant.bodyAcademyKey FROM `Prototype`, `IntegrantPrototype`, `Integrant` WHERE Prototype.urlFile = IntegrantPrototype.urlFile AND IntegrantPrototype.rfc = Integrant.rfc
UNION ALL SELECT Article.urlFile, evidenceType, evidenceTitle, impactBA, registrationResponsible, registrationDate, projectName, Integrant.emailUV, Integrant.bodyAcademyKey FROM `Article`, `IntegrantArticle`, `Integrant` WHERE Article.urlFile = IntegrantArticle.urlFile AND IntegrantArticle.rfc = Integrant.rfc
UNION ALL SELECT Book.urlFile, evidenceType, evidenceTitle, impactBA, registrationResponsible, registrationDate, projectName, Integrant.emailUV, Integrant.bodyAcademyKey FROM `Book`, `IntegrantBook`, `Integrant` WHERE Book.urlFile = IntegrantBook.urlFile AND IntegrantBook.rfc = Integrant.rfc
UNION ALL SELECT ReceptionWork.urlFile, evidenceType, evidenceTitle, impactBA, registrationResponsible, registrationDate, projectName, Integrant.emailUV, Integrant.bodyAcademyKey FROM `ReceptionWork`, `IntegrantReceptionWork`, `Integrant` WHERE ReceptionWork.urlFile = IntegrantReceptionWork.urlFile AND IntegrantReceptionWork.rfc = Integrant.rfc;

CREATE VIEW `Students` AS
SELECT * FROM ArticleStudent UNION ALL SELECT * FROM PrototypeStudent UNION ALL SELECT * FROM BookStudent UNION ALL SELECT * FROM ReceptionWorkStudent;
