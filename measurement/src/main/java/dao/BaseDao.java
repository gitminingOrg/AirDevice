package dao;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;

/**
 * 该类用于管理持久层与数据库的连接sql session, 所有的Dao需要继承此方法
 * Created by sunshine on 4/18/16.
 */
public class BaseDao {
    @Resource
    protected SqlSession sqlSession;

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
}
