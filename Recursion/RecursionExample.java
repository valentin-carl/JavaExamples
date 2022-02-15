public class RekursionExample {

    public static void main (String[] args) throws InterruptedException {

        // Fibonacci series:
        // 0 1 1 2 3 5 8 13 21 ...
        // F(n) = F(n-2)+F(n-1)

        System.out.println(fibonacci(8));
        System.out.println(fibIterative(8));
    }

    static int fibonacci (int n) {
        if (n == 0 || n == 1) return n;
        return fibonacci(n-2)+fibonacci(n-1);
    }

    static int fibIterative (int n) {
        if (n == 0 || n == 1) return n;
        int a = 0, b = 1, index = 2;
        do {
            int temp = b;
            b = a+b;
            a = temp;
            index++;
        } while (index <= n);
        return b;
    }
}

class TreeNode {
    // Attributes
    int value;
    TreeNode leftChild;
    TreeNode rightChild;

    // Constructor
    TreeNode (int value, TreeNode leftChild, TreeNode rightChild) {
        this.value = value;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }
}

class BinaryTree {
    // Attributes
    TreeNode root;

    // Constructor
    BinaryTree (int rootValue) {
        this.root = new TreeNode(rootValue, null, null);
    }
    
    void insert (int value) {
        // call the helper
        this.insertionHelper(value, this.root);
    }

    private void insertionHelper (int value, TreeNode currentNode) {

        if (value >= currentNode.value) {

            // (2) Was soll in diesem Aufruf passieren?
            if (currentNode.rightChild == null) {
                currentNode.rightChild = new TreeNode(value, null, null);

            } else {
                // (3) Nächster rekursiver Aufruf
                this.insertionHelper(value, currentNode.rightChild);
            }

        } else {

            // (2) Was soll in diesem Aufruf passieren?
            if (currentNode.leftChild == null) {
                currentNode.leftChild = new TreeNode(value, null, null);
            } else {
                // (3) Nächster rekursiver Aufruf
                this.insertionHelper(value, currentNode.leftChild);
            }
        }

        // (1) Wann soll die Rekrusion aufhören? (optional)
        return;
    }
}
