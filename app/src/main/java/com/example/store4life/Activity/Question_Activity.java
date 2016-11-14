package com.example.store4life.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.example.store4life.Adapter.ExpandableListAdapter;
import com.example.store4life.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Question_Activity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_ativity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        setTitle("Những Câu Hỏi Thường Gặp");
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        listDataHeader.add("1/ Chính sách giao hàng như thế nào?");
        listDataHeader.add("2/ Chính sách thay đổi hàng như thế nào?");
        listDataHeader.add("3/ Chính sách khách VIP như thế nào?");
        listDataHeader.add("4/Sao tôi có thể tin tưởng khi chuyển tiền rồi, tôi sẽ nhận được hàng?");
        listDataHeader.add("5/ Sao tôi có thể tin tưởng khi chuyển tiền rồi, tôi sẽ nhận được hàng?");
        List<String> cau1 = new ArrayList<String>();
        cau1.add("4Life giao hàng tận nơi trên toàn quốc với chính sách giao hàng cụ thể như sau:\n" +
                "4Life Miễn Phí giao hàng cho đơn hàng trên 1.000.000 VND (1 triệu đồng), đơn hàng dưới 1 triệu đồng 4Life áp dụng mức phí giao hàng theo bảng giá bên dưới:\n" +
                "+ 30k cho khách hàng ở TPHCM\n" +
                "+ 50k cho khách hàng ở các tỉnh");


        List<String> cau2 = new ArrayList<String>();
        cau2.add("KHÁCH MUA HÀNG ONLINE\n" +
                "- Thời gian: trong vòng 15 ngày\n" +
                "- Chính sách: Đổi sản phẩm có giá trị tương đương hoặc lớn hơn\n" +
                "- Cách thức:\n" +
                "     + Quý khách vui lòng gọi về SĐT: hoặc để thông báo\n" +
                "     + Quý khách gửi hàng về địa chỉ: 4Life - …HCM\n" +
                "     + Sau khi nhận được sản phẩm, 4Life sẽ kiểm tra và đổi lại sản phẩm khác cho quý khách");

        List<String> cau3 = new ArrayList<String>();
        cau3.add("Điều điện để được cấp thẻ VIP:\n" +
                "- Hóa đơn thanh toán 01 lần có giá trị từ 1.000.000 đồng trở lên;\n" +
                "- Hoặc tổng giá trị thanh toán từ 01 triệu đồng trở lên trong vòng 01 tháng (quý khách vui lòng giữ hóa đơn để đối chiếu)\n" +
                "Ưu đãi: khách hàng được giảm giá 5% khi mua hàng đối với tất cả các sản phẩm của 4Life\n" +
                "\n" +
                "Sau khi sở hữu thẻ VIP, quý khách được tham gia tích điểm để nhận thẻ VIP ở cấp cao hơn với nhiều chế độ ưu đãi lớn khác. Với 1.000 đồng = 1 điểm");


        List<String> cau4 = new ArrayList<String>();
        cau4.add("Mục đích và phạm vi thu thập thông tin\n" +
                "- Chúng tôi thu thập thông tin từ bạn khi bạn đặt hàng trên trang web hoặc liên hệ email, điện thoại với chúng tôi. Bất kỳ thông tin chúng tôi thu thập từ bạn có thể được sử dụng một trong những cách sau đây:\n" +
                "+ Để cải thiện trang web của chúng tôi (Chúng tôi liên tục cố gắng để cải thiện các dịch vụ trang web của chúng tôi dựa trên các thông tin và phản hồi chúng tôi nhận được từ bạn)\n" +
                "+ Để cải thiện dịch vụ khách hàng (Thông tin của bạn sẽ giúp chúng tôi hiệu quả hơn đáp ứng yêu cầu dịch vụ khách hàng của bạn và nhu cầu hỗ trợ)\n" +
                "+ Để xử lý các giao dịch\n" +
                "+ Địa chỉ email mà bạn cung cấp cho xử lý đơn hàng, có thể được sử dụng để gửi cho bạn thông tin và cập nhật liên quan đến đặt hàng của bạn, ngoài việc tiếp nhận tin tức thường xuyên, cập nhật, sản phẩm hoặc dịch vụ liên quan đến thông tin");

        List<String> cau5 = new ArrayList<String>();
        cau5.add("Khác với một số shop chỉ bán hàng trôi nổi trên mạng không rõ địa chỉ. 4Life là hệ thống cửa hàng có địa chỉ rõ ràng cụ thể được đăng tải trên App của shop. Đặc biệt 4Life sử dụng dịch vụ giao hàng thu tiền tại nhà, khi nào khách hàng nhận được hàng thì mới thanh toán, nên rủi ro là 0%. Uy tín và đảm bảo chất lượng hàng hoá cũng như lợi ích của khách hàng luôn là tiêu chí hàng đầu của 4Life.");

        listDataChild.put(listDataHeader.get(0), cau1);
        listDataChild.put(listDataHeader.get(1), cau2);
        listDataChild.put(listDataHeader.get(2), cau3);
        listDataChild.put(listDataHeader.get(3), cau4);
        listDataChild.put(listDataHeader.get(4), cau5);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
