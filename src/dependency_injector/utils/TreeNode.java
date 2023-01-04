package dependency_injector.utils;

import java.util.ArrayList;
import java.util.List;

// TODO: write more functionality for this
public class TreeNode<T> {

    private final T data;
    private final List<TreeNode<T>> children;
    private TreeNode<T> parent;

    public TreeNode(T data) {
        this.children = new ArrayList<>();
        this.data = data;
    }

    public TreeNode<T> addChild(TreeNode<T> treeNode) {
        children.add(treeNode);
        treeNode.parent = this;
        return this;
    }

    public void addChildren(List<TreeNode<T>> children) {
        for(TreeNode<T> treeNode : children) {
            treeNode.parent = this;
        }
        this.children.addAll(children);
    }
}
