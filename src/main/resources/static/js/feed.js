async function toggleLike(btn) {
    if (!IS_AUTH) {
        showAuthMessage();
        return;
    }

    const postId = btn.dataset.id;
    const res = await fetch(`/api/likes/${postId}`, { method: 'POST' });
    btn.querySelector('span').innerText = await res.json();
}

async function toggleComments(btn) {
    if (!IS_AUTH) {
        showAuthMessage();
        return;
    }

    const post = btn.closest('.post-card');
    const postId = post.dataset.postId;
    const container = post.querySelector('.comments');

    if (!container.classList.contains('hidden')) {
        container.classList.add('hidden');
        container.innerHTML = '';
        return;
    }

    await loadComments(postId);
    container.classList.remove('hidden');
}

async function submitComment(e, postId, parentId = null) {
    if (e.key !== 'Enter' || e.shiftKey) return;

    e.preventDefault();

    const content = e.target.value.trim();
    if (!content) return;

    let url = `/api/comments/${postId}?content=${encodeURIComponent(content)}`;
    if (parentId) url += `&parentId=${parentId}`;

    await fetch(url, { method: 'POST' });

    e.target.value = '';
    await loadComments(postId);
}

async function loadComments(postId) {
    const post = document.querySelector(`[data-post-id="${postId}"]`);
    const container = post.querySelector('.comments');
    const counter = post.querySelector('.comment-btn span');

    const res = await fetch(`/api/comments/${postId}`);
    const data = await res.json();

    counter.innerText = countAllComments(data);

    container.innerHTML = `
        <div class="comment-form">
            <textarea placeholder="Написать комментарий..."
                      onkeydown="submitComment(event, ${postId})"></textarea>
        </div>
        ${data.map(c => renderComment(c)).join('')}
    `;
}

function renderComment(c, level = 0) {
    return `
        <div class="comment" style="margin-left:${level * 24}px">
            <div class="comment-header">
                <a href="/app/user/profile/${c.user.username}" class="comment-user">
                    ${c.user.username}
                </a>
                <span class="comment-date">${formatDate(c.createdAt)}</span>
            </div>

            <div class="comment-text">${c.content}</div>

            <button class="reply-btn" onclick="showReplyForm(${c.id}, this)">Ответить</button>

            <div class="replies">
                ${c.replies ? c.replies.map(r => renderComment(r, level + 1)).join('') : ''}
            </div>
        </div>
    `;
}

function countAllComments(comments) {
    let count = 0;

    for (const c of comments) {
        count++;
        if (c.replies && c.replies.length > 0) {
            count += countAllComments(c.replies);
        }
    }

    return count;
}

function showReplyForm(parentId, btn) {
    const form = document.createElement('div');
    form.className = 'reply-form';
    form.innerHTML = `
        <textarea placeholder="Ваш ответ..."
                  onkeydown="submitComment(event, ${btn.closest('.post-card').dataset.postId}, ${parentId})"></textarea>
    `;
    btn.after(form);
}

function formatDate(str) {
    const d = new Date(str);
    return d.toLocaleString('ru-RU', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

async function follow(username, btn) {
    const res = await fetch(`/api/subscriptions/status/${username}`);
    const isFollow = await res.json();

    await fetch(`/api/subscriptions/${isFollow ? 'unfollow' : 'follow'}/${username}`, { method: 'POST' });

    btn.innerText = isFollow ? 'Подписаться' : 'Отписаться';
}

function showAuthMessage() {
    alert("Сначала войдите в свой аккаунт")
}
