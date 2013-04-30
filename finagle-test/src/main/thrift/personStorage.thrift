
struct Person {
    1: string name,
    2: i32 age
}

enum ErrorCode {
    SYSTEM_ERROR = 1,
    NO_SUCH_PERSON_ERROR = 2
}

exception PersonStorageException {
    1: ErrorCode errorCode
    2: string errorMessage
}

service PersonStorage {
    void store(1: Person person) throws (1: PersonStorageException e),
    Person retrieve(1: string name) throws (1: PersonStorageException e)
}
