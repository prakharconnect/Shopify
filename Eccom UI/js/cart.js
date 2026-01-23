let cart = JSON.parse(localStorage.getItem("cart")) || [];

function loadCart()
{
    cart = JSON.parse(localStorage.getItem("cart")) || [];
    let cartItems = document.getElementById("cart-items");
    let totalAmount=0;
    cartItems.innerHTML="";

    cart.forEach((item,index) => {
        let itemTotal=item.price * item.quantity;
        totalAmount+=itemTotal;

        cartItems.innerHTML +=`
            <tr>
                <td><img src="${item.imageUrl}" width="50"></td>
                <td>${item.name}</td>
                <td>₹${item.price}</td>
                <td>
                    <button class="btn btn-sm btn-secondary" onclick="changeQuantity(${index},-1)">-</button>
                    ${item.quantity}
                    <button class="btn btn-sm btn-secondary" onclick="changeQuantity(${index},1)">+</button>
                </td>
                <td>₹ ${itemTotal}</td>
                <td><button class="btn btn-danger btn-sm" onclick="removeItem(${index})">X</button></td>
            </tr>
        `;
    });

    document.getElementById("total-amount").innerText=totalAmount;
}

function removeItem(index){
    cart.splice(index,1);
    localStorage.setItem("cart", JSON.stringify(cart));
    loadCart();
    updateCartCounter();
}

function addToCart(id,name,price,imageUrl)
{
    let itemIndex=cart.findIndex((item) => item.id===id);

    if(itemIndex!==-1)
    {
        cart[itemIndex].quantity+=1;
    }
    else{
        cart.push({
            id:id,
            name: name,
            price: parseFloat(price),
            imageUrl:imageUrl,
            quantity:1
        });
    }

       let badge = document.querySelector(".cart-badge");
       badge.style.transform = "scale(1.5)";
       badge.style.transition = ".2s";
     setTimeout(()=>badge.style.transform="scale(1)",200);
     showToast("Added to Cart ✅");
     localStorage.setItem("cart",JSON.stringify(cart));
    updateCartCounter();
}

function updateCartCounter()
{
    
    let badge = document.querySelector(".cart-badge");
    badge.innerText = cart.length;

    badge.classList.add("animate");
    setTimeout(()=>badge.classList.remove("animate"),300);
}



function changeQuantity(index,change)
{
    cart[index].quantity+=change;
    if(cart[index].quantity<=0) cart.splice(index,1);
    localStorage.setItem("cart",JSON.stringify(cart));
    loadCart();
    updateCartCounter();
}

async function checkout() {
    if(cart.length === 0){
        alert("Cart is empty!");
        return;
    }

    const email = document.getElementById("userEmail").value;

    if(!email){
        alert("Please enter your email");
        return;
    }

    const req = {
        email: email,
        items: cart.map(item => ({
    productId: item.id,
    quantity: item.quantity,
    price: item.price
}))
    };

    try{
        const res = await fetch("http://localhost:8081/payment/create", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(req)
        });

        const data = await res.json();
        console.log("Payment Order:", data);

        openRazorpay(data, email);

    }catch(err){
        console.log(err);
        alert("Payment service not responding");
    }
}

function openRazorpay(data, email){

    var options = {
        key: data.key,
        amount: data.amount,
        currency: "INR",
        name: "Shopify",
        description: "Order Payment",
        order_id: data.razorpayOrderId,

       handler: async function (response){

    let cartData = JSON.parse(localStorage.getItem("cart")) || [];
    let total = document.getElementById("total-amount").innerText;

    await fetch("http://localhost:8081/payment/verify", {
    method: "POST",
    headers: {
        "Content-Type": "application/json"
    },
    body: JSON.stringify({
       orderId: data.razorpayOrderId,

        paymentId: response.razorpay_payment_id,
        email: document.getElementById("userEmail").value
    })


});

localStorage.setItem("lastOrder", JSON.stringify({
    email: document.getElementById("userEmail").value,
    items: cart,
    total: document.getElementById("total-amount").innerText
}));

localStorage.removeItem("cart");
window.location.href = "success.html";

}
,

        theme:{ color:"#ff6600" }
    };

    var rzp = new Razorpay(options);
    rzp.open();
}

function showToast(msg){
    const t = document.createElement("div");
    t.className="toast-msg";
    t.innerText=msg;
    document.body.appendChild(t);
    setTimeout(()=>t.remove(),2000);
}


document.addEventListener("DOMContentLoaded", loadCart);
document.addEventListener("DOMContentLoaded", updateCartCounter);

