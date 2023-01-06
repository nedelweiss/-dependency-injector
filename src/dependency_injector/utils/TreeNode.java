package dependency_injector.utils;

import java.util.LinkedList;
import java.util.List;

public class TreeNode<T> {

    private final T data;
    private final List<TreeNode<T>> children;
    private TreeNode<T> parent;

    public TreeNode(T data) {
        this.children = new LinkedList<>();
        this.data = data;
    }

    public TreeNode<T> addChild(TreeNode<T> treeNode) {
        children.add(treeNode);
        treeNode.setParent(this);
        return this;
    }

    public void addChildren(List<TreeNode<T>> children) {
        for (TreeNode<T> treeNode : children) {
            treeNode.setParent(this);
        }
        this.children.addAll(children);
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public T getData() {
        return data;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public TreeNode<T> getParent() {
        return parent;
    }
}
