async function toggleFollow(username, btn) {
    const res = await fetch(`/api/subscriptions/status/${username}`);
    const isFollow = await res.json();

    await fetch(`/api/subscriptions/${isFollow ? 'unfollow' : 'follow'}/${username}`, { method: 'POST' });

    await loadFollow(username);
}

async function loadFollow(username) {
    const status = await fetch(`/api/subscriptions/status/${username}`).then(r => r.json());
    const count = await fetch(`/api/subscriptions/count/${username}`).then(r => r.json());

    const btn = document.getElementById('followBtn');
    if (btn) btn.innerText = status ? 'Отписаться' : 'Подписаться';

    document.getElementById('followers').innerText = count;
}

document.addEventListener('DOMContentLoaded', () => {
    const username = document.body.dataset.username;
    if (username) loadFollow(username);
});
