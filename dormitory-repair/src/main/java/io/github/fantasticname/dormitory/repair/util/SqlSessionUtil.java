// package io.github.fantasticname.dormitory.repair.util;



// import org.apache.ibatis.io.Resources;
// import org.apache.ibatis.session.SqlSession;
// import org.apache.ibatis.session.SqlSessionFactory;
// import org.apache.ibatis.session.SqlSessionFactoryBuilder;

// import java.io.IOException;
// import java.io.InputStream;

// public class SqlSessionUtil {

//     private static SqlSessionFactory sqlSessionFactory;


//     // 静态代码块初始化 SqlSessionFactory，保证全局factory只初始化一次
//     static {
//         try {
//             // 加载 MyBatis 配置文件
//             InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
//             sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//         } catch (IOException e) {
//             e.printStackTrace();
//             throw new RuntimeException("初始化 SqlSessionFactory 失败");
//         }
//     }


//     // 构造方法private
//     private SqlSessionUtil() {
//     }



//     /**
//      * 获取 SqlSession（自动提交事务）
//      */
//     public static SqlSession getSqlSession() {
//         return sqlSessionFactory.openSession(true);
//     }

//     /**
//      * 获取 SqlSession（手动控制事务）
//      */
//     public static SqlSession getSqlSession(boolean autoCommit) {
//         return sqlSessionFactory.openSession(autoCommit);
//     }
// }