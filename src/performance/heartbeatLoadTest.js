import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 1,
    duration: '3s',
    thresholds: {
        checks: ['rate>0.99'],
        http_req_failed: ['rate<0.01']
    },
};

const STAGING = __ENV.STAGING === 'true';

const BASE_URL = STAGING
    ? 'https://universal-pocketbase-api.fly.dev/'
    : 'http://localhost:8080';

export default function () {
    console.log('BASE_URL ',BASE_URL)
    const res = http.get(`${BASE_URL}/heartbeat`);

    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    sleep(1);
}
