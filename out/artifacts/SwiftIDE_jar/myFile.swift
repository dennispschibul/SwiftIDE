class ListNode {
    var val: Int 
    var next: ListNode?

    init(val: Int) {
        self.val = val  
    }

    init(val: Int, next: ListNode) {
        self.val = val
        self.next = next
    }
}


var node1 = ListNode(val: 3)
var node2 = ListNode(val: 2, next: node1)
var head: ListNode? = ListNode(val: 1, next: node2)

while(head != nil) {
    print(head!.val)
    head = head!.next
}
