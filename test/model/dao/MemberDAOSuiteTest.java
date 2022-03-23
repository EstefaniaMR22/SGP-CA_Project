package model.dao;

import model.dao.memberdao.AddMemberTest;
import model.dao.memberdao.GetCivilStatusTest;
import model.dao.memberdao.GetMembersTest;
import model.dao.memberdao.RemoveMemberTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(value = Suite.class)
@Suite.SuiteClasses({
        GetCivilStatusTest.class,
        AddMemberTest.class,
        GetMembersTest.class,
        RemoveMemberTest.class,
})
public class MemberDAOSuiteTest {
}
