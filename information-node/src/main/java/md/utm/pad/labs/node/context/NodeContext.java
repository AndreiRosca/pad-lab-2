package md.utm.pad.labs.node.context;

import md.utm.pad.labs.domain.Student;

import java.util.List;

public interface NodeContext {
    int getCollectionSize();

    int getNumberOfConnections();

    int getNodePort();

    List<Student> getAll();
}
