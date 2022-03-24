package model.dao;

import model.dao.memberdao.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(value = Suite.class)
@Suite.SuiteClasses({
        GetCivilStatusTest.class,
        GetStudyGradesTest.class,
        AddMemberTest.class,
        GetMembersTest.class,
        RemoveMemberTest.class,
})
public class MemberDAOSuiteTest {
}
