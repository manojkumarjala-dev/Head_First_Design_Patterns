package MyHashMap;

public class MyHashMap<K,V> {
    public final int INITIAL_CAPACITY = 1<<4;
    public final int MAX_CAPACITY = 1<<30;

    Entry[] hashTable;

    MyHashMap(){
        this.hashTable = new Entry[INITIAL_CAPACITY];
    }

    MyHashMap(int capacity){
        int tableCapacity = getTableCapacityFor(capacity);
        this.hashTable = new Entry[tableCapacity];
    }

    private int getTableCapacityFor(int capacity) {
        if (capacity <= 1) return 1;
        if (capacity >= MAX_CAPACITY) return MAX_CAPACITY;

        // to ensure i get next 2's power and to catch exact 2 powers like (8 1000 -> 0111)
        return Integer.highestOneBit(capacity - 1) << 1;
    }


    class Entry<K,V>{
        K key;
        V value;
        Entry next;

        Entry(K key, V value){
            this.key = key;
            this.value = value;
            this.next = null;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public void setNext(Entry next) {
            this.next = next;
        }

        public Entry getNext() {
            return next;
        }
    }

    public void put(K key, V value){
        int hashCode = key.hashCode() & hashTable.length;
        Entry node = hashTable[hashCode];

        if(node==null){
            Entry newNode = new Entry(key, value);
            hashTable[hashCode] = newNode;
        }
        else{
            Entry prevNode = node;
            while(node!=null){
                if(node.getKey().equals(key)){
                    node.setValue(value);
                    return;
                }
                prevNode = node;
                node = node.getNext();
            }
            Entry newNode = new Entry(key,value);
            prevNode.setNext(newNode);
        }
    }

//    public V get(K key){
//            //
//    }
}
