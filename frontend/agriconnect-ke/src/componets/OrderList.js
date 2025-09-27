import React, { useEffect, useState } from 'react';
import { getOrders, createOrder } from "../services/orderServicerderService";

export default function OrderList() {
    const [orders, setOrders] = useState([]);

    useEffect(() => {
        getOrders().then((res) => setOrders(res.data));
    }, );

    const handleAdd = () => {
        createOrder({
            farmer_id: 1,
            buyer_id: 2,
            deliver_fee: 100.0,
            status: "Pending"
        }).then((res) => setOrders([...orders, res.data]));
    };

    return (
        <div>
            <h2>Orders</h2>
            <button onClick={handleAdd}>Add Sample Order</button>
            <ul>
                {orders.map((o) => (
                    <li key={o.order_id}>
                        Farmer {o.farmer_id} to Buyer {o.buyer_id} | Fee: {o.deliver_fee}
                    </li>
                ))}
            </ul>
        </div>
    );

}