//* Problem  : 
//* Date     : 2009.09.21
//* Author   : alt
//* Tags     : 

#include <stdio.h>
#include <math.h>
#include <string.h>
#include <stdlib.h>
#include <iostream>
#include <numeric>
#include <sstream>
#include <cstdio>
#include <cstdlib>
#include <cmath>
#include <memory>
#include <string>
#include <vector>
#include <cctype>
#include <list>
#include <queue>
#include <deque>
#include <stack>
#include <map>
#include <set>
#include <algorithm>

using namespace std;

//stack
#pragma comment(linker, "/STACK:16177216")

// to disable Visual C++ secure warnings
#pragma warning(disable : 4996)

///BEGIN CUT HERE
// types
typedef long long int64;
typedef unsigned long long uint64;
typedef pair <int,int> PII;
typedef vector <int> VI;
typedef vector <int64> VI64;
typedef vector <string> VS;
typedef vector <vector<string> > VVS;
typedef vector <vector<int> > VVI;
typedef vector <pair<int,int> > VPII;
typedef vector <vector<pair<int,int> > > VVPII;
typedef map<int, int > MII;
typedef map<string, int > MSI;
typedef queue<int > QI;
typedef queue<PII > QPII;
//loops
#define FOR(i, n) for (int i = 0; i < (int)(n); i++)
#define FORR(i, n) for (int i = (int)(n)-1; i >= 0; i--)
#define FORE(i, a, b) for (int i = (int)(a); i <= (int)(b); i++)
#define FORER(i, a, b) for (int i = (int)(a); i >= (int)(b); i--)
#define FORSZ(i, a) FOR(i, SZ(a))
#define FORSZR(i, a) FORR(i, SZ(a))
#define FORO(i, x) for (typeof((x).begin()) i = (x).begin(); i != (x).end(); i++)
#define FOROR(i, x) for (typeof((x).end()) i = (x).end(); i != (x).begin(); i--)
#define REP(n) for (int _foo = (int)(n) - 1; _foo >= 0; _foo--)
#define REP2(n) for (int _foo2 = (int)(n) - 1; _foo2 >= 0; _foo2--)

//sorting & c
#define ALL(a) a.begin(), a.end()
#define RALL(a) a.rbegin(), a.rend()
#define SORT(a) sort(ALL(a))
#define RSORT(a) sort(RALL(a))
#define UNIQUE(c) SORT(c),(c).resize(unique(ALL(c))-(c).begin())
#define REVERSE(a) reverse(ALL(a))
//filling
#define FLA(a, v) memset(a, v, sizeof(a))
#define CLA(a) FLA(a, 0)
//misc
#define MP make_pair
#define PB push_back
#define SZ(a) (int)a.size()
//const
const int INF = 1000000000;
const int64 INFL = 1000000000000000000LL;
const long double PI = acos(-1.0);
const long double EPS = 1E-9;

//some math
template <typename T> inline T gcd(T a, T b)				{ return b ? gcd(b, a % b) : a; }
template <typename T> inline T gcd2(T a, T b)				{ while (a && b) (a > b) ? a %= b : b %= a; return a + b;}
template <typename T> inline T abs(T a)					{ return a < 0 ? -a : a; }
template <typename T> inline T sqr(T a)					{ return a * a; }
template <typename T> inline double hypot(T a, T b)		{ return sqrt(1.0 * a * a + b * b);}
template <typename T> inline T hypot2(T a, T b)			{ return a * a + b * b;}

//assertions
#ifdef _DEBUG
#define ASSERT(f) if (!(f)) printf("%s\n", "ASSERTION FAILED!");
#define ASSERTS(f, s) if (!(f)) printf("%s [%s]\n", "ASSERTION FAILED!", s);
#else
#define ASSERT(f) f
#define ASSERTS(f, s) f
#endif

//input
inline int ri(){int tt; ASSERTS(scanf("%d", &tt) == 1, "ReadInt failed"); return tt;}
inline double rd(){double tt; ASSERTS(scanf("%lf", &tt) == 1, "ReadInt failed"); return tt;}
inline int64 ri64(){int64 tt; ASSERTS(scanf("%lld", &tt) == 1, "ReadInt64 failed"); return tt;}
inline void rs(char *s){ASSERTS(scanf("%s", s) == 1, "ReadChar* failed");}
//output
inline void pvi(int *a, int n){FOR(i, n) printf("%d%c", a[i], i == n - 1 ? '\n' : ' ');}
inline void pi(int n){printf("%d\n", n);}	
inline void pi64(int64 n){printf("%lld\n", n);}

const int dx4[] = {-1, 1, 0, 0};
const int dy4[] = {0, 0, -1, 1};
///END CUT HERE

int n, m, letters, length;


int sz;

string s[10];

struct trie_item{
	int next[64];
	int cnt;
	int f;
}a[128];

class lnum
{
	static const int BASE2 = 10000;
	static const int DIGITS = 4;

	std::vector <int> digits;

	void normalize_size();

public:

	int sign;

	lnum();
	lnum(long long value);
	lnum(const lnum &value);
	lnum(std::string str);

	void addAbs(const lnum& value);
	void subAbs(const lnum& value);
	int cmpAbs(const lnum& value) const;
	int cmpSigned(const lnum& value) const;

	lnum operator+(const lnum& value) const;
	lnum operator-(const lnum& value) const;
	lnum operator*(const lnum& value) const;
	lnum operator/(const lnum& value) const;
	lnum operator/(int value) const;
	lnum operator%(const lnum& value) const;

	lnum& operator+=(const lnum& value);
	lnum& operator-=(const lnum& value);
	lnum& operator*=(const lnum& value);
	lnum& operator/=(const lnum& value);
	lnum& operator/=(int value);
	lnum& operator%=(const lnum& value);

	void operator ++(void);
	void operator --(void);

	bool operator==(const lnum& value) const;
	bool operator!=(const lnum& value) const;
	bool operator<(const lnum& value) const;
	bool operator>(const lnum& value) const;
	bool operator<=(const lnum& value) const;
	bool operator>=(const lnum& value) const;

	lnum Abs();

	std::string toString();
};

/* Constructors */

lnum::lnum(void)
{
	sign = 0;
}

lnum::lnum(long long num)
{
	digits.clear();
	if (num < 0)
	{
		sign = 1; num = -num;
	}
	else sign = 0;
	while (num)
	{
		digits.push_back(num % BASE2);
		num /= BASE2;
	}
}

lnum::lnum(const lnum &value)
{
	sign = value.sign;
	digits.assign(value.digits.begin(), value.digits.end());
}

lnum::lnum(std::string str)
{
	sign = 0;
	if (str[0] == '-') {sign = 1; str.erase(str.begin());}
	int n = str.length();
	digits.reserve(n / 4 + 1);
	for (int i = 0; i < n; )
	{
		int tmp = 0; 
		tmp += str[n-i-1] - '0'; i++;
		if (i < n)
		{
			tmp += 10 * (str[n-i-1] - '0'); i++; 
			if (i < n)
			{
				tmp += 100 * (str[n-i-1] - '0'); i++;
				if (i < n)
				{
					tmp += 1000 * (str[n-i-1] - '0'); i++;
				}
			}
		}
		digits.push_back(tmp);
	}
}

void lnum::addAbs(const lnum& value)
{
	int len = max(value.digits.size(), digits.size()) + 1, len2 = value.digits.size();
	digits.resize(len);
	for (int i = 0, carry = 0; i < len; i++)
	{
		carry += (i < len2 ? value.digits[i] : 0) + digits[i];
		digits[i] = carry % BASE2; carry /= BASE2;
	}
}

void lnum::subAbs(const lnum& value)
{
	int len = max(value.digits.size(), digits.size()), len2 = value.digits.size();
	digits.resize(len);
	for (int i = 0, carry = 0; i < len; i++)
	{
		carry = digits[i] - (i < len2 ? value.digits[i] : 0) - carry;
		digits[i] = (carry  + BASE2) % BASE2;
		carry = (carry < 0);
	}
}

int lnum::cmpAbs(const lnum& value) const
{
	int len1 = digits.size(), len2 = value.digits.size(), len = max(len1, len2);
	for (int i = len - 1; i >= 0; i--)
		if ((i < len1 ? digits[i] : 0) != (i < len2 ? value.digits[i] : 0)) return ((i < len1 ? digits[i] : 0) - (i < len2 ? value.digits[i] : 0));
	return 0;
}

int lnum::cmpSigned(const lnum& value) const
{
	if (value.sign != sign) return value.sign - sign;
	if (sign) return -this->cmpAbs(value);
	return this->cmpAbs(value);
}

lnum lnum::Abs()
{
	lnum res = *this;
	res.sign = 0;
	return res;
}

/* Internal */
void lnum::normalize_size()
{
	int sz;
	while ((sz = digits.size()) && !digits[sz-1]) digits.pop_back();
}

/* Basic Operations */

lnum lnum::operator+(const lnum& value) const
{
	lnum result = *this;
	return result += value;
}


lnum lnum::operator-(const lnum& value) const
{
	lnum result = *this;
	return result -= value;
}

lnum lnum::operator*(const lnum& value) const
{
	lnum result = *this;
	return result *= value;
}

lnum lnum::operator/(const lnum& value) const
{
	lnum result = *this;
	return result /= value;
}

lnum lnum::operator/(int value) const
{
	lnum result = *this;
	return result /= value;
}

lnum lnum::operator%(const lnum& value) const
{
	lnum result = *this;
	return result %= value;
}

lnum& lnum::operator+=(const lnum& value)
{
 	if (value.sign == sign)
		this->addAbs(value);
	else
	{
		int k = this->cmpAbs(value);
		if (!k) {digits.clear(); sign = 0;}
		else if (k > 0)
		{
			subAbs(value);
		}
		else
		{
			lnum res(value);
			res.subAbs(*this);
			sign = res.sign;
			digits = res.digits;
		}
	}
	return *this;
}

lnum& lnum::operator-=(const lnum& value)
{
	if (value.sign != sign)
	{
		this->addAbs(value);
	}
	else
	{
		int k = this->cmpAbs(value);
		if (!k) {digits.clear(); sign = 0;}
		else
		{
			if (k > 0)
			{
				subAbs(value);
			}
			else
			{
				lnum res(value);
				res.subAbs(*this);
				sign = value.sign ^ 1;
				digits = res.digits;
			}
		}
	}
	return *this;
}

lnum& lnum::operator*=(const lnum& value)
{
	int len1 = digits.size(), len2 = value.digits.size(), len = len1 + len2 + 1;
	lnum res; res.digits.resize(len);
	int i, c = 0, j;
	for (i = 0; i < len1; i++)
		for (j = 0; (j < len2) || (c); j++)
		{
			c += (j < len2 ? value.digits[j] : 0) * digits[i] + res.digits[i+j];
			res.digits[i+j] = c % BASE2;
			c /= BASE2;
		}
	res.normalize_size();

	digits = res.digits;
	sign ^= value.sign;
	return *this;
}

lnum& lnum::operator/=(int value)
{
	int i, c = 0, len = digits.size();
	for (i = len - 1; i >= 0; i--)
	{
		c = BASE2 * c + digits[i];
		digits[i] = c / value;
		c %= value;
	}
	normalize_size();
	return *this;
}

lnum& lnum::operator/=(const lnum& value)
{
	lnum l(0), r(*this), one(1);
	while (r - l > one)
	{
		lnum mid(l + r);
		mid /= 2;
		if (mid * value <= *this)
			l = mid;
		else
			r = mid;
	}
	while (l * value <= *this) ++l;
	--l;
	*this = l;
	return *this;
}

lnum& lnum::operator%=(const lnum& value)
{
	lnum div = *this / value;
	*this -= div * value;
	return *this;
}

void lnum::operator++(void)
{
	lnum one(1);
	*this += one;
}

void lnum::operator--(void)
{
	lnum one(1);
	*this -= one;
}

bool lnum::operator==(const lnum& value) const
{
	return !this->cmpSigned(value);
}

bool lnum::operator!=(const lnum& value) const
{
	return this->cmpSigned(value) != 0;
}

bool lnum::operator>(const lnum& value) const
{
	return this->cmpSigned(value) > 0;
}

bool lnum::operator<(const lnum& value) const
{
	return this->cmpSigned(value) < 0;
}

bool lnum::operator>=(const lnum& value) const
{
	return this->cmpSigned(value) >= 0;
}

bool lnum::operator<=(const lnum& value) const
{
	return this->cmpSigned(value) <= 0;
}


std::string lnum::toString()
{
	normalize_size();
	int sz = digits.size();
	if (!sz) return "0";
	char buf[20];
	std::string res;
	if (sign) res += "-";
	sz--;
	sprintf(buf, "%d", digits[sz--]);
	res += buf;
	while (sz >= 0)
	{
		sprintf(buf, "%04d", digits[sz--]);
		res += buf;
	}
	return res;
}

#define matrix VVI

int na = 0;

map<string, int> mp;

char min_char = (' ') + 3;

int map_chars[256];

lnum res;

void build(int p, string prefix)
{
	FOR(i,letters)
	{
		string t(ALL(prefix));
		t.PB((char)(i+min_char));
		if (a[p].next[i])
			build(a[p].next[i], t);
		else
		{
			while (t.length())
			{
				t = string(t.begin() + 1, t.end());
				if (mp[t])
				{
					a[p].next[i] = mp[t];
					break;
				}
			}
		}
	}
}

lnum dp[64][256];

int f[64][256];

VVI adj;

lnum calc(int len, int v)
{
	if (len == 0)
		return v == 0 ? 1 : 0;
	if (!f[len][v])
	{
		f[len][v] = 1;
		lnum res(0);
		FOR(u, sz)
			if (adj[u][v])
				res += calc(len - 1, u) * adj[u][v];
		dp[len][v] = res;
		//cout<<len<<" "<<v<<" = "<<res.toString()<<endl;
	}
	return dp[len][v];
}


void solve()
{
	FOR(i,m)
	{
		int pt = 0;
		const char *ps = s[i].c_str();
		int len = 0;
		while (*ps)
		{
			if (!a[pt].next[*ps-min_char])
				a[pt].next[*ps-min_char] = ++na, a[pt].cnt++;
			pt = a[pt].next[*ps-min_char];
			len++;
			string t(s[i].begin(), s[i].begin() + len);
			mp[t] = pt;
			ps++;
		}
		a[pt].f = 1;
	}
	build(0, "");
	na++;
	sz = n = na;
	adj = VVI(n, VI(n));
	FOR(i, na)
	{
		if (!a[i].f)
		{
			FOR(j, letters)
				adj[i][a[i].next[j]]++;
		}
		else
			adj[i][i] = letters;
	}
	res = lnum(0);
	FOR(i, na) if (a[i].f)
		res += calc(length, i);
	//m = power(m, n);
	//FOR(i, sz) FOR(j, sz) printf("%d%c", adj[i][j], j == sz - 1 ? '\n' : ' ');
	//res = 0;
	lnum t(1);
	REP(length)
		t *= letters;
	res = t - res;
}

bool cmp(string a, string b)
{
	return a.length() < b.length();
}

int main()
{	
#ifdef _DEBUG
	freopen("1064", "r", stdin);
//	freopen("1064o", "w", stdout);
#endif
	char ss[100];
	gets(ss);
	//letters = ri();
	//length = ri();
	//m = ri();
	sscanf(ss, "%d %d %d", &letters, &length, &m);
	gets(ss);
	FOR(i,letters)
	{
		int l = (ss[i]+256)%256;
		map_chars[l] = i;
	}
	FOR(i,m)
	{
		gets(ss);
		char *p = ss;
		while (*p)
			*p++ = map_chars[(*p+256)%256] + min_char;
		s[i] = ss;
	}
	sort(s, s + m, cmp);
	int tm = m; m = 0;
	FOR(i,tm)
	{
		int f = 1;
		FORE(j, 0, i - 1)
			if (strstr(s[i].c_str(), s[j].c_str()))
				f = 0;
		if (f)
			s[m++] = s[i];
	}
	solve();
	puts(res.toString().c_str());
	//pi(res);
	return 0;
}

