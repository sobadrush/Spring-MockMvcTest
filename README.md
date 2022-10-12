# Spring-MockMvcTest
這是一個基於 SpringBoot 測試 MockMVC 的專案
## Spy 與 Mock 的區別

- **@Mock**
    - 對該對象所有非私有方法的調用都沒有調用真實方法
    - 對該對象私有方法的調用無法進行模擬，會調用真實方法
- **@Spy**
    - 對該對象所有方法的調用都直接調用真實方法

- **@InjectMocks**：創建一個實例，簡單的說是這個被標注 @InjectMocks 的物件，可以調用真實代碼的方法，其餘用 @Mock（或 @Spy）註解創建的 mock 物件將被注入到用該實例中

- **@Mock**：對函數的調用均執行 mock（即虛假函數），不執行真正部分

- **@Spy**：對函數的調用均執行真正部分( 可使用```Mockito.doReturn(返回值).when(物件).方法(..) ``` 局部控制 Spy 物件的方法)
