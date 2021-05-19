package cn.mapway.dao;

import org.nutz.dao.*;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.PojoMaker;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Each;
import javax.annotation.Resource;
import java.sql.ResultSet;
import java.util.List;

/**
 * BaseDao
 *
 * @author zhangjianshe@gmail.com
 */
public class BaseDao<T> {

    @Resource
    protected Dao dao;
    Class<T> clazz;

    /**
     * 把刀
     *
     * @return {@link Dao}
     */
    public Dao getDao() {
        return dao;
    }

    /**
     * 元
     *
     * @return 数据源的元数据
     */
    public DatabaseMeta meta() {
        return dao.meta();
    }

    /**
     * 中的
     *
     * @return 一个 Sql 管理接口，你可以通过这个接口管理你自定义的 SQL
     * @see SqlManager
     */
    public SqlManager sqls() {
        return dao.sqls();
    }

    public PojoMaker pojoMaker() {
        return dao.pojoMaker();
    }

    /**
     * 执行
     * 执行一组 Sql，这些 Sql 将会一起被提交
     *
     * @param sqls 要被执行的 Sql 数组
     */
    public void execute(Sql... sqls) {
        dao.execute(sqls);
    }

    /**
     * 运行
     * 这个方法试图给你最大的灵活性，因为你的 ConnCallback 实现类将得到一个 Connection 接口
     * 的实例。请注意，你不需要关闭这个连接，这个函数在退出时会替你关闭连接。
     * <p>
     * 如果你从当前连接对象中创建了ResultSet对象或者 Statement对象，请自行关闭。Nutz.Dao 的原则是：
     * <ul>
     * <li>Nutz.Dao 创建维护的东西 Nutz.Dao 来维护其生命周期
     * <li>你创建的东西 （比如 ResultSet） 你来维护其生命周期
     * </ul>
     *
     * @param callback 回调
     */
    public void run(ConnCallback callback) {
        dao.run(callback);
    }

    /**
     * 得到对象
     * 从一个 ResultSet 中获取一个对象。
     * <p>
     * 因为 Dao 接口可以知道一个 POJO 的映射细节，这个函数可以帮你节省一点体力。
     *
     * @param rs 结果集
     * @param fm 字段过滤器
     * @return 对象
     */
    public T getObject(ResultSet rs, FieldMatcher fm) {
        return dao.getObject(clazz, rs, fm);
    }

    /**
     * 得到对象
     *
     * @param rs     rs
     * @param fm     调频
     * @param prefix 前缀
     * @return {@link T}
     */
    public T getObject(ResultSet rs, FieldMatcher fm, String prefix) {
        return dao.getObject(clazz, rs, fm, prefix);
    }

    /**
     * 插入
     * 将一个对象插入到一个数据源。
     * <p>
     * 声明了 '@Id'的字段会在插入数据库时被忽略，因为数据库会自动为其设值。如果想手动设置，请设置 '@Id(auto=false)'
     * <p>
     * <b>插入之前</b>，会检查声明了 '@Default(@SQL("SELECT ..."))' 的字段，预先执行 SQL 为字段设置。
     * <p>
     * <b>插入之后</b>，会检查声明了 '@Next(@SQL("SELECT ..."))' 的字段，通过执行 SQL 将值取回
     * <p>
     * 如果你的字段仅仅声明了 '@Id(auto=true)'，没有声明 '@Next'，则认为你还是想取回插入后最新的 ID 值，因为
     * 自动为你添加类似 @Next(@SQL("SELECT MAX(id) FROM tableName")) 的设置
     *
     * @param obj 要被插入的对象
     *            <p>
     *            它可以是：
     *            <ul>
     *            <li>普通 POJO
     *            <li>集合
     *            <li>数组
     *            <li>Map
     *            </ul>
     *            <b style=color:red>注意：</b> 如果是集合，数组或者 Map，所有的对象必须类型相同，否则可能会出错
     * @return 插入后的对象
     * @see org.nutz.dao.entity.annotation.Id
     * @see org.nutz.dao.entity.annotation.Default
     * @see org.nutz.dao.entity.annotation.Next
     */
    public T insert(T obj) {
        return dao.insert(obj);
    }

    /**
     * 插入
     * 将一个对象按FieldFilter过滤后,插入到一个数据源。
     * <code>dao.insert(pet, FieldFilter.create(Pet.class, FieldMatcher.create(false)));</code>
     *
     * @param obj    要被插入的对象
     * @param filter 字段过滤器, 其中FieldMatcher.isIgnoreId生效
     * @return 插入后的对象
     * @see Dao#insert(Object)
     */
    public T insert(T obj, FieldFilter filter) {
        return dao.insert(obj, filter);
    }

    /**
     * 插入
     *
     * @param obj     obj
     * @param actived 中共
     * @return {@link T}
     */
    public T insert(T obj, String actived) {
        return dao.insert(obj, actived);
    }

    /**
     * 插入
     * 自由的向一个数据表插入一条数据。数据用名值链描述
     *
     * @param tableName 数据表名
     * @param chain     数据名值链
     */
    public void insert(String tableName, Chain chain) {
        dao.insert(tableName, chain);
    }

    /**
     * 插入
     * 与 insert(String tableName, Chain chain) 一样，不过，数据表名，将取自 POJO 的数据表声明，请参看
     * '@Table' 注解的详细说明
     *
     * @param chain 数据名值链
     * @see org.nutz.dao.entity.annotation.Table
     */
    public void insert(Chain chain) {
        dao.insert(clazz, chain);
    }

    /**
     * 快速插入
     * 快速插入一个对象。 对象的 '@Prev' 以及 '@Next' 在这个函数里不起作用。
     * <p>
     * 即，你必须为其设置好值，它会统一采用 batch 的方法插入
     *
     * @param obj 要被插入的对象
     *            <p>
     *            它可以是：
     *            <ul>
     *            <li>普通 POJO
     *            <li>集合
     *            <li>数组
     *            <li>Map
     *            </ul>
     *            <b style=color:red>注意：</b> 如果是集合，数组或者 Map，所有的对象必须类型相同，否则可能会出错
     * @return {@link T}
     */
    public T fastInsert(T obj) {
        return dao.fastInsert(obj);
    }

    /**
     * 插入与
     * 将对象插入数据库同时，也将符合一个正则表达式的所有关联字段关联的对象统统插入相应的数据库
     * <p>
     * 关于关联字段更多信息，请参看 '@One' | '@Many' | '@ManyMany' 更多的描述
     *
     * @param obj   数据对象
     * @param regex 正则表达式，描述了什么样的关联字段将被关注。如果为 null，则表示全部的关联字段都会被插入
     * @return 数据对象本身
     * @see org.nutz.dao.entity.annotation.One
     * @see org.nutz.dao.entity.annotation.Many
     * @see org.nutz.dao.entity.annotation.ManyMany
     */
    public T insertWith(T obj, String regex) {
        return dao.insertWith(obj, regex);
    }

    /**
     * 插入链接
     * 根据一个正则表达式，仅将对象所有的关联字段插入到数据库中，并不包括对象本身
     *
     * @param obj   数据对象
     * @param regex 正则表达式，描述了什么样的关联字段将被关注。如果为 null，则表示全部的关联字段都会被插入
     * @return 数据对象本身
     * @see org.nutz.dao.entity.annotation.One
     * @see org.nutz.dao.entity.annotation.Many
     * @see org.nutz.dao.entity.annotation.ManyMany
     */
    public T insertLinks(T obj, String regex) {
        return dao.insertLinks(obj, regex);
    }

    /**
     * 插入的关系
     * 将对象的一个或者多个，多对多的关联信息，插入数据表
     *
     * @param obj   对象
     * @param regex 正则表达式，描述了那种多对多关联字段将被执行该操作
     * @return 对象自身
     * @see org.nutz.dao.entity.annotation.ManyMany
     */
    public T insertRelation(T obj, String regex) {
        return dao.insertRelation(obj, regex);
    }

    /**
     * 更新
     * 更新一个对象。对象必须有 '@Id' 或者 '@Name' 或者 '@PK' 声明。
     * <p>
     * 并且调用这个函数前， 主键的值必须保证是有效，否则会更新失败
     * <p>
     * 这个对象所有的字段都会被更新，即，所有的没有被设值的字段，都会被置成 NULL，如果遇到 NOT NULL 约束，则会引发异常。
     * 如果想有选择的更新个别字段，请使用 org.nutz.dao.FieldFilter
     * <p>
     * 如果仅仅想忽略所有的 null 字段，请使用 updateIgnoreNull 方法更新对象
     *
     * @param obj 要被更新的对象
     *            <p>
     *            它可以是：
     *            <ul>
     *            <li>普通 POJO
     *            <li>集合
     *            <li>数组
     *            <li>Map
     *            </ul>
     *            <b style=color:red>注意：</b> 如果是集合，数组或者 Map，所有的对象必须类型相同，否则可能会出错
     * @return 返回实际被更新的记录条数，一般的情况下，如果更新成功，返回 1，否则，返回 0
     * @see FieldFilter
     */
    public int update(T obj) {
        return dao.update(obj);
    }

    /**
     * 更新
     * 更新对象一部分字段
     *
     * @param obj     对象
     * @param actived 正则表达式描述要被更新的字段
     * @return 返回实际被更新的记录条数，一般的情况下，如果更新成功，返回 1，否则，返回 0
     */
    public int update(T obj, String actived) {
        return dao.update(obj, actived);
    }

    /**
     * 更新
     * 更新对象一部分字段
     *
     * @param obj        对象
     * @param actived    正则表达式描述要被更新的字段
     * @param locked     锁着的
     * @param ignoreNull 忽略空
     * @return 返回实际被更新的记录条数，一般的情况下，如果更新成功，返回 1，否则，返回 0
     */
    public int update(T obj, String actived, String locked, boolean ignoreNull) {
        return dao.update(obj, actived, locked, ignoreNull);
    }

    /**
     * 更新
     *
     * @param obj         obj
     * @param fieldFilter 字段过滤
     * @return int
     */
    public int update(T obj, FieldFilter fieldFilter) {
        return dao.update(obj, fieldFilter);
    }

    /**
     * 更新
     *
     * @param obj         obj
     * @param fieldFilter 字段过滤
     * @param cnd         cnd
     * @return int
     */
    public int update(T obj, FieldFilter fieldFilter, Condition cnd) {
        return dao.update(obj, fieldFilter, cnd);
    }

    /**
     * 更新
     *
     * @param obj obj
     * @param cnd cnd
     * @return int
     */
    public int update(T obj, Condition cnd) {
        return dao.update(obj, cnd);
    }

    /**
     * 更新忽略空
     * 更新一个对象，并且忽略所有 null 字段。
     * <p>
     * 注意: 基本数据类型都是不可能为null的,这些字段肯定会更新
     *
     * @param obj 要被更新的对象
     *            <p>
     *            它可以是：
     *            <ul>
     *            <li>普通 POJO
     *            <li>集合
     *            <li>数组
     *            <li>Map
     *            </ul>
     *            <b style=color:red>注意：</b> 如果是集合，数组或者 Map，所有的对象必须类型相同，否则可能会出错
     * @return 返回实际被更新的记录条数，一般的情况下，如果是单一Pojo,更新成功，返回 1，否则，返回 0
     */
    public int updateIgnoreNull(T obj) {
        return dao.updateIgnoreNull(obj);
    }


    /**
     * 更新
     * 与 update(String tableName, Chain chain, Condition cnd) 一样，不过，数据表名，将取自
     * POJO 的数据表声明，请参看 '@Table' 注解的详细说明
     *
     * @param chain 数据名值链
     * @param cnd   WHERE 条件
     * @return 有多少条记录被更新了
     * @see org.nutz.dao.entity.annotation.Table
     */
    public int update(Chain chain, Condition cnd) {
        return dao.update(clazz, chain, cnd);
    }

    /**
     * 更新与
     * 将对象更新的同时，也将符合一个正则表达式的所有关联字段关联的对象统统更新
     * <p>
     * 关于关联字段更多信息，请参看 '@One' | '@Many' | '@ManyMany' 更多的描述
     *
     * @param obj   数据对象
     * @param regex 正则表达式，描述了什么样的关联字段将被关注。如果为 null，则表示全部的关联字段都会被更新
     * @return 数据对象本身
     * @see org.nutz.dao.entity.annotation.One
     * @see org.nutz.dao.entity.annotation.Many
     * @see org.nutz.dao.entity.annotation.ManyMany
     */
    public T updateWith(T obj, String regex) {
        return dao.updateWith(obj, regex);
    }

    /**
     * 更新的链接
     * 根据一个正则表达式，仅更新对象所有的关联字段，并不包括对象本身
     *
     * @param obj   数据对象
     * @param regex 正则表达式，描述了什么样的关联字段将被关注。如果为 null，则表示全部的关联字段都会被更新
     * @return 数据对象本身
     * @see org.nutz.dao.entity.annotation.One
     * @see org.nutz.dao.entity.annotation.Many
     * @see org.nutz.dao.entity.annotation.ManyMany
     */
    public T updateLinks(T obj, String regex) {
        return dao.updateLinks(obj, regex);
    }

    /**
     * 更新关系
     * 多对多关联是通过一个中间表将两条数据表记录关联起来。
     * <p>
     * 而这个中间表可能还有其他的字段，比如描述关联的权重等
     * <p>
     * 这个操作可以让你一次更新某一个对象中多个多对多关联的数据
     *
     * @param regex 正则表达式，描述了那种多对多关联字段将被执行该操作
     * @param chain 针对中间关联表的名值链。
     * @param cnd   针对中间关联表的 WHERE 条件
     * @return 共有多少条数据被更新
     * @see org.nutz.dao.entity.annotation.ManyMany
     */
    public int updateRelation(String regex, Chain chain, Condition cnd) {

        return dao.updateRelation(clazz, regex, chain, cnd);

    }

    /**
     * 查询
     *
     * @param cnd     cnd
     * @param pager   寻呼机
     * @param matcher 匹配器
     * @return {@link List}
     */
    public List<T> query(Condition cnd, Pager pager, FieldMatcher matcher) {
        return dao.query(clazz, cnd, pager, matcher);
    }

    /**
     * 查询
     *
     * @param cnd   cnd
     * @param pager 寻呼机
     * @param regex 正则表达式
     * @return {@link List}
     */
    public List<T> query(Condition cnd, Pager pager, String regex) {
        return dao.query(clazz, cnd, pager, regex);
    }

    /**
     * 查询
     * 查询一组对象。你可以为这次查询设定条件，并且只获取一部分对象（翻页）
     *
     * @param cnd   WHERE 条件。如果为 null，将获取全部数据，顺序为数据库原生顺序
     * @param pager 翻页信息。如果为 null，则一次全部返回. 不会使用cnd中的pager!!!
     * @return 对象列表
     */
    public List<T> query(Condition cnd, Pager pager) {
        return dao.query(clazz, cnd, pager);
    }

    /**
     * 查询
     * 查询一组对象。你可以为这次查询设定条件
     *
     * @param cnd WHERE 条件。如果为 null，将获取全部数据，顺序为数据库原生顺序<br>
     *            只有在调用这个函数的时候， cnd.limit 才会生效
     * @return 对象列表
     */
    public List<T> query(Condition cnd) {
        return dao.query(clazz, cnd);
    }


    /**
     * 每一个
     * 对一组对象进行迭代，这个接口函数非常适用于很大的数据量的集合，因为你不可能把他们都读到内存里
     *
     * @param cnd      WHERE 条件。如果为 null，将获取全部数据，顺序为数据库原生顺序
     * @param pager    翻页信息。如果为 null，则一次全部返回
     * @param callback 处理回调
     * @return 一共迭代的数量
     */
    public int each(Condition cnd, Pager pager, Each<T> callback) {
        return dao.each(clazz, cnd, pager, callback);
    }

    /**
     * 每一个
     * 对一组对象进行迭代，这个接口函数非常适用于很大的数据量的集合，因为你不可能把他们都读到内存里
     *
     * @param cnd      WHERE 条件。如果为 null，将获取全部数据，顺序为数据库原生顺序
     * @param callback 处理回调
     * @return 一共迭代的数量
     */
    public int each(Condition cnd, Each<T> callback) {
        return dao.each(clazz, cnd, callback);
    }


    /**
     * 删除
     * 根据对象 ID 删除一个对象。它只会删除这个对象，关联对象不会被删除。
     * <p>
     * 你的对象必须在某个字段声明了注解 '@Id'，否则本操作会抛出一个运行时异常
     * <p>
     * 如果你设定了外键约束，没有正确的清除关联对象会导致这个操作失败
     *
     * @param id 对象 ID
     * @return 影响的行数
     * @see org.nutz.dao.entity.annotation.Id
     */
    public int delete(long id) {
        return dao.delete(clazz, id);
    }

    /**
     * 删除
     * 根据对象 Name 删除一个对象。它只会删除这个对象，关联对象不会被删除。
     * <p>
     * 你的对象必须在某个字段声明了注解 '@Name'，否则本操作会抛出一个运行时异常
     * <p>
     * 如果你设定了外键约束，没有正确的清除关联对象会导致这个操作失败
     *
     * @param name 对象 Name
     * @return 影响的行数
     * @see org.nutz.dao.entity.annotation.Name
     */
    public int delete(String name) {
        return dao.delete(clazz, name);
    }

    /**
     * deletex
     * 根据复合主键，删除一个对象。该对象必须声明 '@PK'，并且，给定的参数顺序 必须同 '@PK' 中声明的顺序一致，否则会产生不可预知的错误。
     *
     * @param pks 复合主键需要的参数，必须同 '@PK'中声明的顺序一致
     * @return int
     */
    public int deletex(Object... pks) {
        return dao.deletex(clazz, pks);
    }


    /**
     * 获取
     * 根据对象 ID 获取一个对象。它只会获取这个对象，关联对象不会被获取。
     * <p>
     * 你的对象必须在某个字段声明了注解 '@Id'，否则本操作会抛出一个运行时异常
     *
     * @param id 对象 ID
     * @return {@link T}
     * @see org.nutz.dao.entity.annotation.Id
     */
    public T fetch(long id) {
        return dao.fetch(clazz, id);
    }

    /**
     * 获取
     * 根据对象 Name 获取一个对象。它只会获取这个对象，关联对象不会被获取。
     * <p>
     * 你的对象必须在某个字段声明了注解 '@Name'，否则本操作会抛出一个运行时异常
     *
     * @param name 对象 Name
     * @return 对象本身
     * @see org.nutz.dao.entity.annotation.Name
     */
    public T fetch(String name) {
        return dao.fetch(clazz, name);
    }

    /**
     * fetchx
     * 根据复合主键，获取一个对象。该对象必须声明 '@PK'，并且，给定的参数顺序 必须同 '@PK' 中声明的顺序一致，否则会产生不可预知的错误。
     *
     * @param pks 复合主键需要的参数，必须同 '@PK'中声明的顺序一致
     * @return {@link T}
     */
    public T fetchx(Object... pks) {
        return dao.fetchx(clazz, pks);
    }

    /**
     * 获取
     * 根据 WHERE 条件获取一个对象。如果有多个对象符合条件，将只获取 ResultSet 第一个记录
     *
     * @param cnd WHERE 条件
     * @return 对象本身
     * @see Condition
     * @see org.nutz.dao.entity.annotation.Name
     */
    public T fetch(Condition cnd) {
        return dao.fetch(clazz, cnd);
    }


    /**
     * 清晰的
     * 根据一个 WHERE 条件，清除一组对象。只包括对象本身，不包括关联字段
     *
     * @param cnd 查询条件，如果为 null，则全部清除
     * @return 影响的行数
     */
    public int clear(Condition cnd) {
        return dao.clear(clazz, cnd);
    }


    /**
     * 得到实体
     * 获取实体描述, 其中包含了Java Pojo数据库的全部映射信息
     *
     * @return 实体描述
     */
    public Entity<T> getEntity() {
        return dao.getEntity(clazz);
    }

    /**
     * 数
     * 根据条件，计算某个对象在数据库中有多少条记录
     *
     * @param cnd WHERE 条件
     * @return 数量
     */
    public int count(Condition cnd) {
        return dao.count(clazz, cnd);
    }

    /**
     * 计算某个对象在数据库中有多少条记录
     *
     * @return 数量
     */
    public int count() {
        return dao.count(clazz);
    }


    /**
     * 得到最大id
     * 获取某个对象，最大的 ID 值。这个对象必须声明了 '@Id'
     *
     * @return 最大 ID 值
     */
    public int getMaxId() {
        return dao.getMaxId(clazz);
    }


    /**
     * 创建寻呼机
     * 根据数据源的类型，创建一个翻页对象
     *
     * @param pageNumber 第几页 ，从 1 开始。
     * @param pageSize   每页可以有多少条记录
     * @return 翻页对象
     */
    public Pager createPager(int pageNumber, int pageSize) {
        return dao.createPager(pageNumber, pageSize);
    }


    /**
     * 执行
     * 执行单条自定义SQL
     *
     * @param sql 自定义SQL对象
     * @return 传入的SQL对象, 方便链式调用
     */
    public Sql execute(Sql sql) {
        return dao.execute(sql);
    }


    /**
     * 插入
     * 以特殊规则执行insert
     *
     * @param t              实例对象
     * @param ignoreNull     忽略空值
     * @param ignoreZero     忽略0值
     * @param ignoreBlankStr 忽略空白字符串
     * @return 传入的实例变量
     */
    public T insert(T t, boolean ignoreNull, boolean ignoreZero, boolean ignoreBlankStr) {
        return dao.insert(t, ignoreNull, ignoreZero, ignoreBlankStr);
    }

    /**
     * 插入或更新
     * 根据对象的主键(@Id/@Name/@Pk)先查询, 如果存在就更新, 不存在就插入
     *
     * @param t 对象
     * @return 原对象
     */
    public T insertOrUpdate(T t) {
        return dao.insertOrUpdate(t);
    }

    /**
     * 插入或更新
     * 根据对象的主键(@Id/@Name/@Pk)先查询, 如果存在就更新, 不存在就插入
     *
     * @param t                 对象
     * @param insertFieldFilter 插入时的字段过滤, 可以是null
     * @param updateFieldFilter 更新时的字段过滤,可以是null
     * @return 原对象
     */
    public T insertOrUpdate(T t, FieldFilter insertFieldFilter, FieldFilter updateFieldFilter) {
        return dao.insertOrUpdate(t, insertFieldFilter, updateFieldFilter);
    }

    /**
     * 如果匹配更新和增加
     * 乐观锁, 以特定字段的值作为限制条件,更新对象,并自增该字段.

     * 执行的sql如下:

     * <code>update t_user set age=30, city="广州", version=version+1 where name="wendal" and version=124;</code>
     *
     * @param obj         需要更新的对象, 必须带@Id/@Name/@Pk中的其中一种.
     * @param fieldFilter 需要过滤的属性. 若设置了哪些字段不更新,那务必确保过滤掉fieldName的字段
     * @param fieldName   参考字段的Java属性名.默认是"version",可以是任意数值字段
     * @return 若更新成功, 返回值大于0, 否则小于等于0
     */
    public int updateAndIncrIfMatch(T obj, FieldFilter fieldFilter, String fieldName) {
        return dao.updateAndIncrIfMatch(obj, fieldFilter, fieldName);
    }

}
