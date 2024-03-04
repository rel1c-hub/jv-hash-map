package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final int DEFAULT_GROWTH_FACTOR = 2;
    private static final int DEFAULT_INITIAL_SIZE = 0;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getIndexByKey(key);
        Node<K, V> nodeTemp = table[index];

        if (nodeTemp == null) {
            table[index] = newNode;
        } else {
            while (true) {
                if (Objects.equals(nodeTemp.key, key)) {
                    nodeTemp.value = value;
                    return;
                } else if (nodeTemp.next == null) {
                    nodeTemp.next = newNode;
                    break;
                }
                nodeTemp = nodeTemp.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndexByKey(key);
        Node<K,V> nodeTemp = table[index];
        while (nodeTemp != null) {
            if (Objects.equals(nodeTemp.key, key)) {
                return nodeTemp.value;
            }
            nodeTemp = nodeTemp.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexByKey(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void checkSize() {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        size = DEFAULT_INITIAL_SIZE;
        int newLength = table.length * DEFAULT_GROWTH_FACTOR;
        Node<K, V>[] tableStorage = table;
        table = new Node[newLength];

        for (Node<K, V> tempNode : tableStorage) {
            while (tempNode != null) {
                put(tempNode.key, tempNode.value);
                tempNode = tempNode.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        private boolean hasNext() {
            return next != null;
        }
    }
}
