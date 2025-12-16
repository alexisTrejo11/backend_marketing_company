-- ===========================================
-- Customer Companies Table Migration
-- ===========================================
CREATE TABLE IF NOT EXISTS customer_companies (
    id BIGSERIAL PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    legal_name VARCHAR(255),
    tax_id VARCHAR(50),
    website VARCHAR(500),
    founding_year INTEGER,
    industry_code VARCHAR(20) NOT NULL,
    industry_name VARCHAR(255),
    sector VARCHAR(100),
    company_size VARCHAR(20),
    employee_count INTEGER,
    annual_revenue_amount NUMERIC(15,2),
    annual_revenue_currency CHAR(3),
    revenue_range VARCHAR(20),
    status VARCHAR(20) NOT NULL,
    is_public_company BOOLEAN DEFAULT FALSE,
    is_startup BOOLEAN DEFAULT FALSE,
    target_market VARCHAR(1000),
    mission_statement VARCHAR(2000),

    -- Audit Fields
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    -- Constraints for unique fields
    CONSTRAINT uq_customer_companies_tax_id UNIQUE (tax_id),

    -- Constraints for validation
    CONSTRAINT ck_company_size_valid
        CHECK (company_size IS NULL OR
               company_size IN ('MICRO', 'SMALL', 'MEDIUM', 'LARGE', 'ENTERPRISE')),

    CONSTRAINT ck_status_valid
        CHECK (status IN ('LEAD', 'PROSPECT', 'ACTIVE', 'INACTIVE', 'CHURNED', 'SUSPENDED')),

    CONSTRAINT ck_founding_year_valid
        CHECK (founding_year IS NULL OR
               (founding_year >= 1500 AND founding_year <= EXTRACT(YEAR FROM CURRENT_DATE))),

    CONSTRAINT ck_employee_count_valid
        CHECK (employee_count IS NULL OR employee_count >= 0),

    CONSTRAINT ck_annual_revenue_valid
        CHECK (annual_revenue_amount IS NULL OR annual_revenue_amount >= 0),

    CONSTRAINT ck_currency_valid
        CHECK (annual_revenue_currency IS NULL OR
               annual_revenue_currency ~ '^[A-Z]{3}$'),

    CONSTRAINT ck_revenue_range_valid
        CHECK (revenue_range IS NULL OR
               revenue_range IN ('MICRO', 'SMALL', 'MEDIUM', 'LARGE', 'ENTERPRISE', 'UNKNOWN')),

    CONSTRAINT ck_contract_dates_valid
        CHECK (contract_start_date IS NULL OR contract_end_date IS NULL OR
               contract_start_date <= contract_end_date),

    CONSTRAINT ck_monthly_fee_valid
        CHECK (monthly_fee IS NULL OR monthly_fee >= 0),

    CONSTRAINT ck_website_valid
        CHECK (website IS NULL OR website ~ '^https?://[^\s/$.?#].[^\s]*$'),

    CONSTRAINT ck_email_format
        CHECK (billing_email IS NULL OR
               billing_email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),

    CONSTRAINT ck_tax_id_format
        CHECK (tax_id IS NULL OR LENGTH(tax_id) >= 5),

    CONSTRAINT ck_payment_method_valid
        CHECK (preferred_payment_method IS NULL OR
               preferred_payment_method IN ('CREDIT_CARD', 'BANK_TRANSFER', 'PAYPAL', 'INVOICE', 'CASH'))
);

-- ===========================================
-- Contact Persons Table
-- ===========================================
CREATE TABLE IF NOT EXISTS contact_persons (
    id BIGSERIAL,
    company_id VARCHAR(36) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50),
    position VARCHAR(100),
    department VARCHAR(100),
    is_decision_maker BOOLEAN DEFAULT FALSE,
    is_primary_contact BOOLEAN DEFAULT FALSE,
    notes VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Constraints
    CONSTRAINT pk_contact_persons PRIMARY KEY (id),

    CONSTRAINT fk_contact_persons_company
        FOREIGN KEY (company_id)
        REFERENCES customer_companies(id)
        ON DELETE CASCADE,

    CONSTRAINT ck_contact_names_length
        CHECK (LENGTH(first_name) >= 2 AND LENGTH(last_name) >= 2),

    CONSTRAINT ck_contact_email_format
        CHECK (email IS NULL OR
               email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),

    CONSTRAINT ck_contact_phone_format
        CHECK (phone IS NULL OR phone ~ '^\+?[0-9\s\-\(\)]{7,20}$')
);

-- ===========================================
-- TABLE FOR KEY PRODUCTS (ElementCollection)
-- ===========================================
CREATE TABLE IF NOT EXISTS company_key_products (
    company_id VARCHAR(36) NOT NULL,
    product VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_company_key_products PRIMARY KEY (company_id, product),
    CONSTRAINT fk_company_key_products_company
        FOREIGN KEY (company_id)
        REFERENCES customer_companies(id)
        ON DELETE CASCADE
);

-- ===========================================
-- TABLE FOR COMPETITORS (ElementCollection)
-- ===========================================
CREATE TABLE IF NOT EXISTS company_competitors (
    company_id VARCHAR(36) NOT NULL,
    competitor_url VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_company_competitors PRIMARY KEY (company_id, competitor_url),
    CONSTRAINT fk_company_competitors_company
        FOREIGN KEY (company_id)
        REFERENCES customer_companies(id)
        ON DELETE CASCADE,

    CONSTRAINT ck_competitor_url_format
        CHECK (competitor_url ~ '^https?://[^\s/$.?#].[^\s]*$')
);


-- ===========================================
-- INDEX FOR CUSTOMER_COMPANIES
-- ===========================================

-- INDEX FOR UNIQUE FIELDS
CREATE INDEX IF NOT EXISTS idx_companies_name_search
    ON customer_companies USING gin (to_tsvector('english', company_name));

CREATE INDEX IF NOT EXISTS idx_companies_legal_name
    ON customer_companies(legal_name)
    WHERE legal_name IS NOT NULL;

-- INDEX FOR STATUS AND DATES
CREATE INDEX IF NOT EXISTS idx_companies_status_date
    ON customer_companies(status, created_at DESC)
    WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_companies_industry_sector
    ON customer_companies(industry_code, sector);

CREATE INDEX IF NOT EXISTS idx_companies_size_revenue
    ON customer_companies(company_size, revenue_range);

CREATE INDEX IF NOT EXISTS idx_companies_employee_range
    ON customer_companies(employee_count)
    WHERE employee_count IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_companies_revenue_amount
    ON customer_companies(annual_revenue_amount DESC)
    WHERE annual_revenue_amount IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_companies_founding_year
    ON customer_companies(founding_year DESC)
    WHERE founding_year IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_companies_public_startup
    ON customer_companies(is_public_company, is_startup);

CREATE INDEX IF NOT EXISTS idx_companies_contract_active
    ON customer_companies(is_active, contract_end_date)
    WHERE is_active = TRUE;

-- INDEX FOR CONTRACT DATES
CREATE INDEX IF NOT EXISTS idx_companies_contract_dates
    ON customer_companies(contract_start_date, contract_end_date)
    WHERE contract_start_date IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_companies_contract_expiring
    ON customer_companies(contract_end_date)
    WHERE contract_end_date > CURRENT_DATE
    AND contract_end_date < CURRENT_DATE + INTERVAL '90 days';

-- INDEX FOR AUDIT FIELDS
CREATE INDEX IF NOT EXISTS idx_companies_created_date
    ON customer_companies(DATE(created_at));

CREATE INDEX IF NOT EXISTS idx_companies_updated_date
    ON customer_companies(DATE(updated_at));

CREATE INDEX IF NOT EXISTS idx_companies_deleted
    ON customer_companies(deleted_at)
    WHERE deleted_at IS NOT NULL;

-- Index for searching multiple text fields
CREATE INDEX IF NOT EXISTS idx_companies_fulltext_search
    ON customer_companies USING GIN (
        to_tsvector('english',
            coalesce(company_name, '') || ' ' ||
            coalesce(legal_name, '') || ' ' ||
            coalesce(industry_name, '') || ' ' ||
            coalesce(sector, '') || ' ' ||
            coalesce(target_market, '')
        )
    );

-- Functional Index for Email Domain Search
CREATE INDEX IF NOT EXISTS idx_companies_billing_email_domain
    ON customer_companies(SUBSTRING(billing_email FROM '@(.*)$'))
    WHERE billing_email IS NOT NULL;

-- ===========================================
-- INDEX FOR CONTACT_PERSONS
-- ===========================================

-- Main Indexes
CREATE INDEX IF NOT EXISTS idx_contacts_company
    ON contact_persons(company_id);

CREATE INDEX IF NOT EXISTS idx_contacts_email
    ON contact_persons(email)
    WHERE email IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_contacts_name
    ON contact_persons(LOWER(first_name || ' ' || last_name));

CREATE INDEX IF NOT EXISTS idx_contacts_decision_maker
    ON contact_persons(company_id)
    WHERE is_decision_maker = TRUE;

CREATE INDEX IF NOT EXISTS idx_contacts_primary_contact
    ON contact_persons(company_id)
    WHERE is_primary_contact = TRUE;

CREATE INDEX IF NOT EXISTS idx_contacts_position_dept
    ON contact_persons(position, department)
    WHERE position IS NOT NULL;

-- Índice para búsqueda combinada
CREATE INDEX IF NOT EXISTS idx_contacts_full_search
    ON contact_persons USING GIN (
        to_tsvector('english',
            coalesce(first_name, '') || ' ' ||
            coalesce(last_name, '') || ' ' ||
            coalesce(position, '') || ' ' ||
            coalesce(department, '')
        )
    );

-- ===========================================
-- ÍNDICES FOR KEY PRODUCTS AND COMPETITORS
-- ===========================================
CREATE INDEX IF NOT EXISTS idx_key_products_company
    ON company_key_products(company_id);

CREATE INDEX IF NOT EXISTS idx_competitors_company
    ON company_competitors(company_id);

-- Index for full-text search on key products
CREATE INDEX IF NOT EXISTS idx_key_products_search
    ON company_key_products USING gin (to_tsvector('english', product));