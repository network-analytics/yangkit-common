package com.huawei.yang.common.api;

public class Link<E> {
   private LinkNode header;
   private SortOrder sortOrder;
   private int size;

   public Link(SortOrder sortOrder) {
      this();
      this.sortOrder = sortOrder;
   }

   public Link() {
      this.sortOrder = SortOrder.NATURAL;
      this.header = new LinkNode();
      this.header.setNext((LinkNode)null);
      this.size = 0;
   }

   public int size() {
      return this.size;
   }

   public LinkNode getHeader() {
      return this.header;
   }

   public boolean isEmpty() {
      if (null == this.header) {
         return true;
      } else {
         return null == this.header.getNext();
      }
   }

   public boolean insert(E data) {
      if (null == data) {
         return false;
      } else {
         LinkNode node = new LinkNode((ILinkData)data);
         return this.insert(node);
      }
   }

   public boolean insert(E data, int index) {
      if (null == data) {
         return false;
      } else if (this.sortOrder != SortOrder.NATURAL) {
         return false;
      } else {
         LinkNode node = new LinkNode((ILinkData)data);
         return this.insert(node, index);
      }
   }

   private boolean insert(LinkNode node, int index) {
      if (null == node) {
         return false;
      } else if (null == node.getData()) {
         return false;
      } else if (null == this.header) {
         return false;
      } else if (index < 0) {
         return false;
      } else if (0 == this.size) {
         return this.insert(node);
      } else if (index >= this.size) {
         return false;
      } else {
         LinkNode firstNode = this.header.getNext();
         if (null == firstNode) {
            return false;
         } else {
            LinkNode curNode = firstNode;

            for(int i = 0; i < index; ++i) {
               curNode = curNode.getNext();
               if (null == curNode) {
                  return false;
               }
            }

            LinkNode preNode = curNode.getPre();
            if (null != preNode) {
               preNode.setNext(node);
               node.setPre(preNode);
               node.setNext(curNode);
               curNode.setPre(node);
               ++this.size;
            } else {
               this.header.setNext(node);
               node.setPre((LinkNode)null);
               node.setNext(firstNode);
               firstNode.setPre(node);
               ++this.size;
            }

            return true;
         }
      }
   }

   private boolean insert(LinkNode node) {
      if (null == node) {
         return false;
      } else if (null == node.getData()) {
         return false;
      } else if (null == this.header) {
         return false;
      } else if (this.isEmpty()) {
         this.header.setNext(node);
         node.setNext((LinkNode)null);
         node.setPre((LinkNode)null);
         ++this.size;
         return true;
      } else {
         for(LinkNode curNode = this.header.getNext(); null != curNode; curNode = curNode.getNext()) {
            LinkNode preNode = curNode.getPre();
            LinkNode nextNode = curNode.getNext();
            ILinkData curData = curNode.getData();
            ILinkData insertData = node.getData();
            if (this.sortOrder != SortOrder.NATURAL) {
               if (curData.compare(insertData) < 0) {
                  if (this.sortOrder == SortOrder.DESCEND) {
                     if (null == preNode) {
                        this.header.setNext(node);
                        node.setPre((LinkNode)null);
                     } else {
                        node.setPre(preNode);
                        preNode.setNext(node);
                     }

                     node.setNext(curNode);
                     curNode.setPre(node);
                     ++this.size;
                     return true;
                  }
               } else if (this.sortOrder == SortOrder.ASCEND) {
                  if (null == preNode) {
                     this.header.setNext(node);
                     node.setPre((LinkNode)null);
                  } else {
                     preNode.setNext(node);
                     node.setPre(preNode);
                  }

                  node.setNext(curNode);
                  curNode.setPre(node);
                  ++this.size;
                  return true;
               }
            }

            if (null == nextNode) {
               curNode.setNext(node);
               node.setPre(curNode);
               node.setNext((LinkNode)null);
               ++this.size;
               return true;
            }
         }

         return false;
      }
   }

   private LinkNode getNode(int index) {
      if (null == this.header) {
         return null;
      } else if (index >= this.size) {
         return null;
      } else {
         LinkNode firstNode = this.header.getNext();
         if (null == firstNode) {
            return null;
         } else {
            LinkNode curNode = firstNode;

            for(int i = 0; i < index; ++i) {
               curNode = curNode.getNext();
               if (null == curNode) {
                  return null;
               }
            }

            return curNode;
         }
      }
   }

   public E get(int index) {
      LinkNode node = this.getNode(index);
      return null == node ? null : (E)(node.getData());
   }

   public E getNext(int index) {
      LinkNode curNode = this.getNode(index);
      if (null == curNode) {
         return null;
      } else {
         curNode = curNode.getNext();
         return null == curNode ? null : (E)(curNode.getData());
      }
   }

   public boolean contains(E data) {
      if (null == data) {
         return false;
      } else if (null == this.header) {
         return false;
      } else if (null == this.header.getNext()) {
         return false;
      } else {
         for(LinkNode curNode = this.header.getNext(); null != curNode; curNode = curNode.getNext()) {
            if (curNode.getData().equals(data)) {
               return true;
            }
         }

         return false;
      }
   }

   public int getIndex(E data) {
      if (null == data) {
         return -1;
      } else if (null == this.header) {
         return -1;
      } else if (null == this.header.getNext()) {
         return -1;
      } else {
         LinkNode curNode = this.header.getNext();

         for(int index = 0; null != curNode; ++index) {
            if (curNode.getData().equals(data)) {
               return index;
            }

            curNode = curNode.getNext();
         }

         return -1;
      }
   }

   public LinkNode remove(E data) {
      if (null == data) {
         return null;
      } else if (null == this.header) {
         return null;
      } else if (null == this.header.getNext()) {
         return null;
      } else {
         for(LinkNode curNode = this.header.getNext(); null != curNode; curNode = curNode.getNext()) {
            if (curNode.getData().equals(data)) {
               LinkNode preNode = curNode.getPre();
               LinkNode nextNode = curNode.getNext();
               if (null == preNode) {
                  this.header.setNext(nextNode);
               } else {
                  preNode.setNext(nextNode);
               }

               if (null != nextNode) {
                  nextNode.setPre(preNode);
               }

               --this.size;
               return curNode;
            }
         }

         return null;
      }
   }

   public E remove(int index) {
      if (null == this.header) {
         return null;
      } else if (index >= this.size) {
         return null;
      } else {
         LinkNode firstNode = this.header.getNext();
         if (null == firstNode) {
            return null;
         } else {
            LinkNode curNode = firstNode;

            for(int i = 0; i < index; ++i) {
               curNode = curNode.getNext();
               if (null == curNode) {
                  return null;
               }
            }

            LinkNode preNode = curNode.getPre();
            LinkNode nextNode = curNode.getNext();
            if (null == preNode) {
               this.header.setNext(nextNode);
            } else {
               preNode.setNext(nextNode);
            }

            if (null != nextNode) {
               nextNode.setPre(preNode);
            }

            --this.size;
            return (E)(curNode.getData());
         }
      }
   }

   public boolean move(E src, E dst) {
      if (null == src) {
         return false;
      } else if (src == dst) {
         return true;
      } else {
         this.remove(src);
         if (null == dst) {
            return this.insert(src);
         } else {
            int index = this.getIndex(dst);
            if (-1 == index) {
               return false;
            } else {
               return index + 1 == this.size ? this.insert(src) : this.insert(src, index + 1);
            }
         }
      }
   }

   public boolean move(int srcIndex, int dstIndex) {
      if (srcIndex == dstIndex) {
         return true;
      } else {
         E srcData = this.remove(srcIndex);
         if (srcIndex < dstIndex) {
            return dstIndex == this.size ? this.insert(srcData) : this.insert(srcData, dstIndex);
         } else {
            return this.insert(srcData, dstIndex + 1);
         }
      }
   }

   public static enum SortOrder {
      ASCEND,
      DESCEND,
      NATURAL;
   }
}
