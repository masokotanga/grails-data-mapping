package org.grails.datastore.mapping.mongo

import grails.gorm.dirty.checking.DirtyCheck
import org.junit.Test

class EnumPersistenceSpec extends AbstractMongoTest {

    @Test
    void testBasicPersistenceOperations() {
        md.mappingContext.addPersistentEntity(TestEnumEntity)

        AbstractMongoSession session = md.connect()

        session.nativeInterface.dropDatabase(session.defaultDatabase)

        def te = new TestEnumEntity(name:"Bob", type:TestType.T1)

        session.persist te
        session.flush()

        assert te != null
        assert te.id != null
        assert te.id instanceof Long

        session.clear()
        te = session.retrieve(TestEnumEntity, te.id)

        assert te != null
        assert te instanceof TestEnumEntity
        assert te.name == "Bob"
        assert te.type instanceof TestType
        assert te.type == TestType.T1

        te.type = TestType.T2
        session.persist(te)
        session.flush()
        session.clear()

        te = session.retrieve(TestEnumEntity, te.id)
        assert te != null
        assert te.type == TestType.T2
    }
}

@DirtyCheck
class TestEnumEntity {
    Long id
    String name
    TestType type
}

enum TestType {
    T1, T2, T3
}
