package model.dao;

import model.dao.lgacdao.AddLGACDAOTest;
import model.dao.lgacdao.GetAllLGACSTest;
import model.dao.lgacdao.GetLGACByIDTest;
import model.dao.lgacdao.RemoveLGACDAOTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(value = Suite.class)
@Suite.SuiteClasses({
        AddLGACDAOTest.class,
        RemoveLGACDAOTest.class,
        GetAllLGACSTest.class,
        GetLGACByIDTest.class
})
public class LGACDaoSuiteTest { }
